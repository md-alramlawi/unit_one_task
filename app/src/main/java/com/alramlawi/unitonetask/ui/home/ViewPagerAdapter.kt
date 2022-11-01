package com.alramlawi.unitonetask.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.viewpager.widget.PagerAdapter
import com.alramlawi.unitonetask.R
import com.alramlawi.unitonetask.models.Slider
import com.alramlawi.unitonetask.utils.bindImageFromUrl
import java.util.*


class ViewPagerAdapter(private val context: Context, private val list: List<Slider>) : PagerAdapter() {
    override fun getCount(): Int {
        return list.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as LinearLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val mLayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val itemView: View = mLayoutInflater.inflate(R.layout.slider_item, container, false)
        val imageView: ImageView = itemView.findViewById<View>(R.id.imv_slider) as ImageView
        bindImageFromUrl(imageView, list[position].imageURL)
        Objects.requireNonNull(container).addView(itemView)

        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }
}