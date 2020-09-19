package github.sachin2dehury.nitrresources.core

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import github.sachin2dehury.nitrresources.R
import github.sachin2dehury.nitrresources.core.Core.changeFragment
import github.sachin2dehury.nitrresources.ui.fragment.LoginFragment
import kotlinx.android.synthetic.main.activity_nav_drawer.*


class NavDrawerActivity : AppCompatActivity() {

    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav_drawer)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.app_name, R.string.app_name)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        nav_drawer.setNavigationItemSelectedListener { item ->
            true
        }

        Core.fragmentManager = supportFragmentManager
        changeFragment(LoginFragment())

        MobileAds.initialize(this) {}
        val adRequest = AdRequest.Builder().build()!!
        adView.loadAd(adRequest)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}