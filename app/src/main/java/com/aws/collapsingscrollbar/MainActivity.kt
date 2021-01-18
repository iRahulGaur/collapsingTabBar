package com.aws.collapsingscrollbar

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.aws.collapsingscrollbar.databinding.ActivityMainBinding
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import java.lang.StrictMath.abs

class MainActivity : AppCompatActivity(), OnOffsetChangedListener {

    private lateinit var binding: ActivityMainBinding

    private var collapsedMenu: Menu? = null
    private var appBarExpanded = true

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val toolbar = findViewById<Toolbar>(R.id.mainToolbarView)
        setSupportActionBar(toolbar)
        if (supportActionBar != null) supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        toolbar.title = "Group name"
        toolbar.subtitle = "user1, user2, user3, user4, user5"

        binding.mainCollapsingToolbarView.setExpandedTitleTextAppearance(R.style.MyStyle_White)
        binding.mainCollapsingToolbarView.setCollapsedTitleTextAppearance(R.style.MyStyleSmall)
        binding.mainCollapsingToolbarView.setExpandedSubtitleTextAppearance(R.style.MyStyleSmall_White)
        binding.mainCollapsingToolbarView.setCollapsedSubtitleTextAppearance(R.style.MyStyleSmall)

        binding.mainAppBarLayout.addOnOffsetChangedListener(OnOffsetChangedListener { _, verticalOffset ->
            //  Vertical offset == 0 indicates appBar is fully expanded.
            if (kotlin.math.abs(verticalOffset) > 200) {
                appBarExpanded = false
                invalidateOptionsMenu()
            } else {
                appBarExpanded = true
                invalidateOptionsMenu()
            }
        })
    }

    abstract class AppBarStateChangeListener : OnOffsetChangedListener {

        enum class State {
            EXPANDED, COLLAPSED, IDLE
        }

        private var mCurrentState = State.IDLE

        override fun onOffsetChanged(appBarLayout: AppBarLayout, i: Int) {
            if (i == 0 && mCurrentState != State.EXPANDED) {
                onStateChanged(appBarLayout, State.EXPANDED)
                mCurrentState = State.EXPANDED
            } else if (abs(i) >= appBarLayout.totalScrollRange && mCurrentState != State.COLLAPSED) {
                onStateChanged(appBarLayout, State.COLLAPSED)
                mCurrentState = State.COLLAPSED
            } else if (mCurrentState != State.IDLE) {
                onStateChanged(appBarLayout, State.IDLE)
                mCurrentState = State.IDLE
            }
        }

        abstract fun onStateChanged(
            appBarLayout: AppBarLayout?,
            state: State?
        )

    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {

    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        if (collapsedMenu != null
            && (!appBarExpanded || collapsedMenu!!.size() != 1)
        ) {
            //collapsed
        } else {
            //expanded

        }
        return super.onPrepareOptionsMenu(collapsedMenu)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        collapsedMenu = menu
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.action_settings -> {
                Toast.makeText(this, "More clicked", Toast.LENGTH_SHORT).show()
                return true
            }
        }
        if (item.title === "Add") {
            Toast.makeText(this, "clicked add", Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }

}