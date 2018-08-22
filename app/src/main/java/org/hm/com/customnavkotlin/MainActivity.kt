package org.hm.com.customnavkotlin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.widget.DrawerLayout
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),CustomNavigationView.NavigationItemSelectionListener {

    override fun onItemSelected(view: View, position: Int) {

        when(position)
        {
            0->Toast.makeText(this,"One",Toast.LENGTH_SHORT).show()
            1->Toast.makeText(this,"Two",Toast.LENGTH_SHORT).show()
            2->Toast.makeText(this,"Three",Toast.LENGTH_SHORT).show()
        }
        drawerLayout.closeDrawers()
    }

    override fun onHeaderSelected(view: View) {
    }

    var menuList : MutableList<String> = mutableListOf()
    lateinit var arrayAdapter: ArrayAdapter<String>
    lateinit var customNavigationView: CustomNavigationView<String>
    lateinit var drawerLayout : DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        customNavigationView = findViewById(R.id.navView)
        drawerLayout = findViewById(R.id.activity_main)

        prepareListItems()

        arrayAdapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,menuList)

        customNavigationView.setAdapter(arrayAdapter,android.R.color.white)

       // customNavigationView.setHeaderView(getHeader(),20)

        customNavigationView.setBackgroundSelection(ContextCompat.getColor(this,R.color.colorAccent))
        customNavigationView.setOnNavigationItemSelectionListener(this)
        customNavigationView.setNavScrollState(CustomNavigationView.FULL_SCROLLABLE)

        drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener
        {
            override fun onDrawerStateChanged(newState: Int) {

            }

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

                drawerLayout.getChildAt(0).translationX = slideOffset * drawerView.width
                drawerLayout.bringChildToFront(drawerView)
                drawerView.requestLayout()
            }

            override fun onDrawerClosed(drawerView: View) {
            }

            override fun onDrawerOpened(drawerView: View) {
            }

        })
    }



    private fun prepareListItems() {
        menuList.add("One")
        menuList.add("Two")
        menuList.add("Three")
    }
}
