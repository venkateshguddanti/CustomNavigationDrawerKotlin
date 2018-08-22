package org.hm.com.customnavkotlin

import android.annotation.SuppressLint
import android.app.backup.FullBackupDataOutput
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.support.design.internal.ScrimInsetsFrameLayout
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ScrollView
import com.squareup.picasso.Picasso
import android.graphics.drawable.BitmapDrawable
import android.graphics.Bitmap
import android.view.LayoutInflater
import com.squareup.picasso.Target
import java.lang.Exception

@SuppressLint("RestrictedApi")

class CustomNavigationView<T> @JvmOverloads constructor(
        context: Context,
        attributeSet: AttributeSet
  ) : ScrimInsetsFrameLayout(context,attributeSet) ,Target
{
     var scrollView: ScrollView
     lateinit var arrayAdapter: ArrayAdapter<T>
     lateinit var drawableBackGround : Drawable
     var linearLayoutOptions : LinearLayout
     var mainLinearLayout : LinearLayout
     lateinit var childViews : Array<View>
    var backGroundColor = android.R.color.holo_red_dark
     companion object {
        val FULL_SCROLLABLE : Int = -1
        val MENU_SCROLLABLE : Int = -2
    }

    var scrollState : Int = FULL_SCROLLABLE
    var isHeaderDrawn : Boolean = false
    lateinit var mListener : NavigationItemSelectionListener

     var myView : CustomNavigationView<T>

    init {

        myView = this
        val listParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
        scrollView = ScrollView(context)
        linearLayoutOptions = LinearLayout(context)
        mainLinearLayout = LinearLayout(context)
        linearLayoutOptions.orientation = LinearLayout.VERTICAL
        linearLayoutOptions.layoutParams = listParams
        mainLinearLayout.orientation = LinearLayout.VERTICAL
        mainLinearLayout.layoutParams = listParams
        listParams.gravity = Gravity.START
        scrollView.layoutParams = listParams
        scrollView.isVerticalFadingEdgeEnabled = true
        scrollView.isHorizontalScrollBarEnabled = true

       /* if(drawableBackGround != null) {
            background = drawableBackGround
        }else
        {
            setBackgroundColor(Color.parseColor("0xffffffff"))
        }
       */
        scrollView.addView(linearLayoutOptions)
        mainLinearLayout.addView(scrollView)
        addView(mainLinearLayout)
        fitsSystemWindows = true

    }


    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {

    }

    override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {

    }

    override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
        background = BitmapDrawable(resources,bitmap)
    }



    fun setAdapter( adapter : ArrayAdapter<T>,defBackground : Int)
    {
           this.arrayAdapter = adapter

           childViews = Array(adapter.count,{view ->View(context)})

            for(i in childViews.indices)
            {
                  childViews[i] = adapter.getView(i,null,this)
                  childViews[i].setTag(i)
                  val childView = childViews[i]
                  childView.setOnClickListener(OnClickListener {

                      for (innerIndex in childViews.indices)
                      {
                          if(childView.tag == innerIndex)
                          {
                              childViews[innerIndex].setBackgroundColor(backGroundColor)
                          }else
                          {
                              childViews[innerIndex].setBackgroundColor(defBackground)
                          }
                      }

                      mListener?.let {  mListener.onItemSelected(childView,i) }


                  })

                linearLayoutOptions.addView(childView)
            }


    }
    /**
     * Sets the header view for the navigation drawer along with the bottom margin
     *
     * @param view         header view for the navigation drawer
     * @param marginBottom bottom margin of header view
     */
    fun setHeaderView(view: View,bottomMargin : Int)
    {
        if(scrollState == FULL_SCROLLABLE)
        {
            val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
            params.setMargins(0,0,0,bottomMargin)
            view.layoutParams = params
            view.setOnClickListener(OnClickListener {
                mListener?.let { mListener.onHeaderSelected(view) }
            })
            linearLayoutOptions.addView(view,0)



        }else if(scrollState == MENU_SCROLLABLE)
        {
            val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
            params.setMargins(0,0,0,bottomMargin)
            view.layoutParams = params
            view.setOnClickListener(OnClickListener {
                mListener?.let { mListener.onHeaderSelected(view) }
            })
            mainLinearLayout.addView(view,0)

        }
        isHeaderDrawn = true
    }

    /**
     * Sets the specified drawable to the background of navigation drawer
     *
     * @param backGround drawable for the background
     */
    fun setBackGround( drawable: Drawable)
    {
        drawableBackGround = drawable
        background = drawableBackGround
    }

    /**
     * Sets the specified URL to the background of navigation drawer
     *
     * @param backGround drawable for the background
     */
    fun setBackGround(url : String)
    {
        Picasso.get().load(url).into(myView)
    }

    fun setBackgroundSelection(selectionBackground : Int)
    {
        backGroundColor = selectionBackground
    }

    /**
     * Returns the nav item adapter
     *
     * @return ListAdapter
     */
    fun getAdapter() : ArrayAdapter<T>
    {
        return arrayAdapter
    }


    /**
     * interface to notify click actions on navigation items
     */
    interface NavigationItemSelectionListener
    {
        fun onItemSelected(view: View,position : Int)
        fun onHeaderSelected(view: View)
    }

    /**
     * Sets a item click listner,which notifies the click actions on the action items
     *
     * @param navigationItemSelectedListner listner notifies item click with particular position
     */
    fun setOnNavigationItemSelectionListener(mListener : NavigationItemSelectionListener)
    {
         this.mListener = mListener
    }
    /**
     * Sets scroll state to navigation view
     *
     * @param state FULL_SCROLLABLE - whole layout scrollable
     *              MENU_ITEM_SCROLLABLE - only menu items will be scrollable
     */
    fun setNavScrollState(state : Int)
    {
       if(this.scrollState != state)
       {
           if(isHeaderDrawn)
           {
               if(state == FULL_SCROLLABLE)
               {
                   val removeView = linearLayoutOptions.getChildAt(0)
                   linearLayoutOptions.removeViewAt(0)
                   mainLinearLayout.addView(removeView,0)
               }else if(state == MENU_SCROLLABLE)
               {
                   val removeView = mainLinearLayout.getChildAt(0)
                   mainLinearLayout.removeViewAt(0)
                   linearLayoutOptions.addView(removeView,0)
               }
           }
       }
    }


}