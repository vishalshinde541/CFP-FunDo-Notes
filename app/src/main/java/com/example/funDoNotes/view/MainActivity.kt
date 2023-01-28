package com.example.funDoNotes.view

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuItemCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.loginandregistrationwithfragment.R
import com.example.loginandregistrationwithfragment.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity(), NavDrawerHandler {

    lateinit var toggle: ActionBarDrawerToggle
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView
    lateinit var toolbar: Toolbar
    lateinit var binding: ActivityMainBinding
    private lateinit var fAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fAuth = FirebaseAuth.getInstance()
        val uid = fAuth.currentUser?.uid

        supportFragmentManager.beginTransaction().replace(R.id.fragmentsContainer, LoginFragment())
            .commit()
        val currentUser = fAuth.currentUser
        if (currentUser != null) {
            val addToBackStack = supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentsContainer, HomePageFragment()).addToBackStack(null)
                .commit()
        } else {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentsContainer, LoginFragment())
                .commit()
        }

        // Navigation Drawer
        drawerLayout = findViewById(R.id.drawerLayout)
        navView = findViewById(R.id.nav_view)
        toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle("Fun Do")

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        fun signOut() {
            fAuth.signOut()
            replaceFragment(LoginFragment(), title.toString())
        }

        navView.setNavigationItemSelectedListener {
            it.isChecked = true
            when (it.itemId) {

                R.id.nav_home -> replaceFragment(HomePageFragment(), it.title.toString())
                R.id.nav_reminders -> replaceFragment(RemindersFragment(), it.title.toString())
                R.id.nav_archive -> replaceFragment(ArchiveFragment(), it.title.toString())
                R.id.nav_setting -> replaceFragment(SettingFragment(), it.title.toString())
                R.id.nav_helpFeedback -> Toast.makeText(
                    applicationContext,
                    "clicked Help and feedback",
                    Toast.LENGTH_SHORT
                ).show()
                R.id.nav_logOut -> signOut()
                R.id.nav_share -> Toast.makeText(
                    applicationContext,
                    "clicked Share",
                    Toast.LENGTH_SHORT
                ).show()
                R.id.nav_rateus -> Toast.makeText(
                    applicationContext,
                    "clicked Rate us",
                    Toast.LENGTH_SHORT
                ).show()
            }
            true
        }

    }

    private fun replaceFragment(fragment: Fragment, title: String) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentsContainer, fragment)
        fragmentTransaction.commit()
        drawerLayout.closeDrawers()
        setTitle(title)
    }

    private fun addFragment() {
        val fragmentManager = supportFragmentManager
        val dialogProfileFragment = DialogProfileFragment()
        dialogProfileFragment.show(fragmentManager, "dialogProfile")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.opt_menu, menu)
        val menuItem: MenuItem? = menu?.findItem(R.id.opt_profile_Image)
        val view = MenuItemCompat.getActionView(menuItem)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (toggle.onOptionsItemSelected(item)) {
            return true
        } else if (
            return when (item.itemId) {

                R.id.opt_profile_Image -> {
                    addFragment()
                    Toast.makeText(this, "Clicked on profile", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> return super.onOptionsItemSelected(item)
            }
        )
            toggle.syncState()
        return super.onOptionsItemSelected(item)
    }
    fun checkForInternet(context: Context): Boolean {

        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

                else -> false
            }
        } else {
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }

    override fun setDrawerLocked() {
        super.setDrawerLocked()
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }


}