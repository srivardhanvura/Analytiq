package com.example.analytiq.fragment


import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.analytiq.R

class AboutUsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_about_us, container, false)

        view.findViewById<ImageView>(R.id.imageView3).setOnClickListener {
            val uri = Uri.parse("https://www.instagram.com/_u/analytiqapp")
            val insta = Intent(Intent.ACTION_VIEW, uri)
            insta.setPackage("com.instagram.android")

            try {
                startActivity(insta)
            } catch (e: ActivityNotFoundException) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://www.instagram.com/analytiqapp")
                    )
                )
            }
        }

        view.findViewById<ImageView>(R.id.imageView4).setOnClickListener {

            try {
                (activity as Context).packageManager.getPackageInfo("com.facebook.katana", 0)
                val uri = Uri.parse("fb://page/101544861531915/")
                val face = Intent(Intent.ACTION_VIEW, uri)
                startActivity(face)
            } catch (e: ActivityNotFoundException) {
                val uri = Uri.parse("https://www.facebook.com/101544861531915/")
                val face = Intent(Intent.ACTION_VIEW, uri)
                startActivity(face)
            }
        }

        view.findViewById<ImageView>(R.id.imageView5).setOnClickListener {
            try {
                (activity as Context).packageManager.getPackageInfo("com.twitter.android", 0)
                val uri = Uri.parse("twitter://user?user_id=1250133484934316035")
                val twit = Intent(Intent.ACTION_VIEW, uri)
                startActivity(twit)
            } catch (e: ActivityNotFoundException) {
                val uri = Uri.parse("https://twitter.com/QAnalyti")
                val twit = Intent(Intent.ACTION_VIEW, uri)
                startActivity(twit)
            }
        }

        view.findViewById<ImageView>(R.id.imageView0).setOnClickListener {
            val list = arrayOf("analytiqapp@gmail.com")
            val mail = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, list)
                putExtra(Intent.EXTRA_SUBJECT, "Regarding Analytiq app")
            }

            try {
                startActivity(Intent.createChooser(mail, "Choose an app"))
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(
                    activity as Context,
                    "This device does not have any email clients",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        view.findViewById<ImageView>(R.id.imageView6).setOnClickListener {
            try {
                (activity as Context).packageManager.getPackageInfo("com.linkedin.android", 0)
                val linked = Intent(Intent.ACTION_VIEW, Uri.parse("linkedin://company/analytiqapp"))
                startActivity(linked)
            } catch (e: ActivityNotFoundException) {
                val linked =
                    Intent(Intent.ACTION_VIEW, Uri.parse("http://linkedin.com/company/analytiqapp"))
                startActivity(linked)
            }
        }
        return view
    }
}

