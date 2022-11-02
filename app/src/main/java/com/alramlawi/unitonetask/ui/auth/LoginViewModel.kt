package com.alramlawi.unitonetask.ui.auth

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class LoginViewModel: ViewModel() {

    private var storedVerificationId: String? = ""
    private var resendToken: PhoneAuthProvider.ForceResendingToken? = null
    private var auth: FirebaseAuth? = null

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?>
        get() = _errorMessage

    private val _showOptDialog = MutableLiveData<Boolean>(false)
    val showOptDialog: LiveData<Boolean>
        get() = _showOptDialog

    private val _isLogin = MutableLiveData<Boolean>(false)
    val isLogin: LiveData<Boolean>
        get() = _isLogin

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {

        }
        override fun onVerificationFailed(e: FirebaseException) {
            _errorMessage.postValue(e.message ?: e.localizedMessage)
        }
        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
            storedVerificationId = verificationId
            resendToken = token
            _showOptDialog.postValue(true)
        }

        override fun onCodeAutoRetrievalTimeOut(p0: String) {
            _errorMessage.postValue(p0)
        }
    }

    init {
        auth = FirebaseAuth.getInstance()
    }


    fun sendCode(activity: Activity, phone: String){
        viewModelScope.launch {
            auth?.let {
                val options = PhoneAuthOptions.newBuilder()
                    .setPhoneNumber(phone)       // Phone number to verify
                    .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                    .setActivity(activity)                 // Activity (for callback binding)
                    .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
                    .build()
                PhoneAuthProvider.verifyPhoneNumber(options)
            }
        }

    }



    fun verifyPhoneNumberWithCode(activity: Activity, code: String) {
        viewModelScope.launch {
            val credential = PhoneAuthProvider.getCredential(storedVerificationId!!, code)
            signInWithPhoneAuthCredential(activity, credential)
        }
    }

    private fun signInWithPhoneAuthCredential(activity: Activity, credential: PhoneAuthCredential) {
        viewModelScope.launch {
            auth?.signInWithCredential(credential)?.addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    val user = task.result?.user
                    _isLogin.postValue(user != null)

                } else {
                    _errorMessage.postValue(
                        task.exception?.message ?:
                        task.exception?.localizedMessage ?:
                        "Failed to login")
                }
            }
        }
    }

    fun notifyDialogIsShown(){
        _showOptDialog.postValue(false)
    }


}