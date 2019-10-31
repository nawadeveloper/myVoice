package np.com.nawarajbista.myvoice.ui.home

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_home.*
import np.com.nawarajbista.myvoice.MainActivity
import np.com.nawarajbista.myvoice.Post
import np.com.nawarajbista.myvoice.R
import np.com.nawarajbista.myvoice.UserDataFireBase
import java.lang.Exception
import java.text.Format
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import kotlin.time.nanoseconds

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
//        val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(this, Observer {
//            textView.text = it
        })


        val myActivity = activity as MainActivity
        val ref = myActivity.getUserData()


        ref.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val user = dataSnapshot.getValue(UserDataFireBase::class.java)

                try {
                    Picasso.get().load(user?.defaultProfilePicture).into(image_user)
                    textview_user_name.text = user?.fullName

                } catch (e: Exception) {
                    Log.d("homeFragment", "${e.message}")
                }

            }
        })





        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button_record.setOnClickListener {
            val currentUser = FirebaseAuth.getInstance().currentUser?.uid
            val dbRef = FirebaseDatabase.getInstance().getReference("/users/$currentUser")

            val status = edittext_status.text.toString()
            val date = Calendar.getInstance().time
            val formatter = SimpleDateFormat.getDateTimeInstance()
            val currentDate = formatter.format(date)


            if(status.isNotEmpty()) {
                val post = Post(currentDate, status)
                val reference = dbRef.child("post").push()

                reference.setValue(post)
            }
            else {
                Toast.makeText(context, "write some status.", Toast.LENGTH_SHORT).show()
            }



        }
    }




}