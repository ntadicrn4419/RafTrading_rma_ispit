package rs.raf.jun.presentation.view.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import rs.raf.jun.R
import rs.raf.jun.presentation.view.fragments.DiscoverFragment
import rs.raf.jun.presentation.view.fragments.PortfolioFragment
import rs.raf.jun.presentation.view.fragments.ProfileFragment

class MainPagerAdapter(
    fragmentManager: FragmentManager,
    private val context: Context,
) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    companion object {
        private const val ITEM_COUNT = 3
        const val FRAGMENT_1 = 0
        const val FRAGMENT_2 = 1
        const val FRAGMENT_3 = 2
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            FRAGMENT_1 -> DiscoverFragment()
            FRAGMENT_2 -> PortfolioFragment()
            else -> ProfileFragment()
        }
    }

    override fun getCount(): Int {
        return ITEM_COUNT
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            FRAGMENT_1 -> context.getString(R.string.discover_fragment_title)
            FRAGMENT_2 -> context.getString(R.string.portfolio_fragment_title)
            else -> context.getString(R.string.profile_fragment_title)
        }
    }
}