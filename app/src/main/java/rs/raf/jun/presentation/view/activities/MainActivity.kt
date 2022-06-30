package rs.raf.jun.presentation.view.activities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import rs.raf.jun.R
import rs.raf.jun.databinding.ActivityMainBinding
import rs.raf.jun.presentation.view.fragments.MainFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init() {
        initFragment()
    }


    private fun initFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setReorderingAllowed(true)
        transaction.add(R.id.main_fragment_container, MainFragment())
        transaction.commit()
    }

}