package com.example.analytiq.fragment


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.analytiq.R
import com.example.analytiq.adapter.TeamAdapter
import com.example.analytiq.model.TeamListData

class TeamsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_team, container, false)

        val list = ArrayList<TeamListData>()
        list.add(
            TeamListData(
                "Deepak Goyal",
                "St. Xavier's College\nCEO at RBeI\nFounder of AnalytiQ\nSpecialisation in Finance",
                R.drawable.dg
            )
        )
        list.add(
            TeamListData(
                "Radhika Rathi",
                "B.Com M.Com\nAdministrative Officer at RBeI\nCo-Owner of AnalytiQ\nSpecialisation in Management and Accountancy",
                R.drawable.rr
            )
        )
        list.add(
            TeamListData(
                "V.Srivardhan",
                "Manipal Institute of Technology\nSr.Backend Developer\nTeam Head\nProgrammer,Frontend and Backend App Development",
                R.drawable.vra
            )
        )
        list.add(
            TeamListData(
                "Monu Kumar",
                "IIT BHU(B.Tech Mining Engg.)\nSr.Backend Developer\nSetup Manager\nProgrammer,Android Developer & Tech Enthusiast",
                R.drawable.kmonu
            )
        )
        list.add(
            TeamListData(
                "Doyel Mishra",
                "University Institute of Technology,B.U\nSr.UI Developer\nUI and UX Team Leader\nAndroid App Development",
                R.drawable.babi
            )
        )

        list.add(
            TeamListData(
                "Komal Bang",
                "Women Engg. College, Ajmer(B.Tech)\nUI Developer\nMarketing Head\nSpecialization in Programming & Frontend App Development",
                R.drawable.kbang
            )
        )

        list.add(
            TeamListData(
                "Thangadipelli Apoorva",
                "B.Tech 3rd Year\nAssociate Developer\nSpecialization in C, C++, Java, Python, DBMS, HTML & CSS",
                R.drawable.appu
            )
        )

        list.add(
            TeamListData(
                "K.Pooja",
                "Rungta College of Engg. and Development(BE),Bhilai\nAssociate Developer\nProgrammer,Android Developer & Web Development skills",
                R.drawable.pooja
            )
        )

        val adapter = TeamAdapter(list)
        val layout = LinearLayoutManager(activity as Context)
        val recycler = view.findViewById<RecyclerView>(R.id.team_recycler)
        recycler.adapter = adapter
        recycler.layoutManager = layout

        return view
    }
}
