package com.example.analytiq.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.analytiq.R
import com.example.analytiq.fragment.AboutUsFragment
import com.example.analytiq.fragment.HomeFragment
import com.example.analytiq.fragment.TeamsFragment
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import java.security.Permission

class MainActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar
    lateinit var nav_view: NavigationView
    lateinit var frame_layout: FrameLayout
    lateinit var drawer_layout: DrawerLayout
    lateinit var mAuth: FirebaseAuth

    val STORAGE_PERMISSION = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestStoragePermissions()
        }
        mAuth = FirebaseAuth.getInstance()

        nav_view = findViewById(R.id.main_nav_view)
        toolbar = findViewById(R.id.main_toolbar)
        frame_layout = findViewById(R.id.main_frame_layout)
        drawer_layout = findViewById(R.id.main_drawer)

        val prev: MenuItem? = null
        setUpToolbar()

        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            drawer_layout,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        supportFragmentManager.beginTransaction().replace(R.id.main_frame_layout, HomeFragment())
            .commit()
        supportActionBar?.title = "Home"
        nav_view.setCheckedItem(R.id.nav_home)

        nav_view.setNavigationItemSelectedListener {

            if (prev != null)
                prev.isChecked = false
            it.isCheckable = true
            it.isChecked = true

            when (it.itemId) {

                R.id.nav_home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frame_layout, HomeFragment())
                        .commit()
                    supportActionBar?.title = "Home"
                    drawer_layout.closeDrawers()
                }

                R.id.nav_team -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frame_layout, TeamsFragment())
                        .commit()
                    supportActionBar?.title = "Team"
                    drawer_layout.closeDrawers()
                }
                R.id.nav_info -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frame_layout, AboutUsFragment())
                        .commit()
                    supportActionBar?.title = "About Us"
                    drawer_layout.closeDrawers()
                }

                R.id.nav_logout -> {
                    val alert = AlertDialog.Builder(this)
                    alert.setTitle("Logout?")
                    alert.setMessage("Are you sure you want to logout?")
                    alert.setPositiveButton("Yes") { t, l ->
                        mAuth.signOut()
                        finish()
                        startActivity(Intent(this, Login_Form::class.java))
                    }
                    alert.setNegativeButton("Cancel") { t, l -> }
                    alert.create()
                    alert.show()
                    drawer_layout.closeDrawers()
                }
            }
            return@setNavigationItemSelectedListener true
        }

    }

    fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Home"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId
        if (id == android.R.id.home) {
            drawer_layout.openDrawer(GravityCompat.START)
        }

        return super.onOptionsItemSelected(item)
    }

    fun requestStoragePermissions() {
        val permi = Array<String>(1, { i -> "" })
        permi[0] = Manifest.permission.READ_EXTERNAL_STORAGE
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {
            val alert = AlertDialog.Builder(this)
            alert.setTitle("Storage permission required")
            alert.setMessage("Storage permission required to import excel files")
            alert.setPositiveButton("Ok") { text, listener ->
                ActivityCompat.requestPermissions(this@MainActivity, permi, STORAGE_PERMISSION)
            }
            alert.setNegativeButton("Cancel") { text, listener -> }
            alert.create()
            alert.show()
        } else {
            ActivityCompat.requestPermissions(this, permi, STORAGE_PERMISSION)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == STORAGE_PERMISSION) {
            if (!(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onBackPressed() {

        val frag = supportFragmentManager.findFragmentById(R.id.main_frame_layout)

        when (frag) {
            !is HomeFragment -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_frame_layout, HomeFragment())
                    .commit()
                supportActionBar?.title = "Home"
                nav_view.setCheckedItem(R.id.nav_home)
            }

            else -> super.onBackPressed()
        }
    }
}
