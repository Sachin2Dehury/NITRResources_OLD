package github.sachin2dehury.nitrresources.ui.fragment

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import github.sachin2dehury.nitrresources.R
import github.sachin2dehury.nitrresources.core.Core.page
import github.sachin2dehury.nitrresources.ui.adapter.PageAdapter
import kotlinx.android.synthetic.main.fragment_page.*

class PageFragment : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.fragment_page)

        val pages = listOf("Notes", "Assignment", "Slides")
        viewPager.adapter = PageAdapter(pages)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = pages[position]
            page = pages[position]
        }.attach()

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
    }
}