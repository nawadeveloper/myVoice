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
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_home.*
import np.com.nawarajbista.myvoice.*
import np.com.nawarajbista.myvoice.R
import java.lang.Exception
import java.text.Format
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.HashMap
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


        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val adaptor = GroupAdapter<GroupieViewHolder>()
        val userFriendList = arrayListOf<String>()

        val currentUser = FirebaseAuth.getInstance().currentUser?.uid
        val userRef = FirebaseDatabase.getInstance().getReference("users/$currentUser")

        //get friend list of current user
        userRef.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.d("HomeFragment", p0.message)
            }

            override fun onDataChange(userData: DataSnapshot) {

                adaptor.clear()
                userFriendList.clear()

                val userInformation = userData.getValue(UserInformation::class.java)

                userInformation?.friendList?.forEach {
                    userFriendList.add(it.key)
                }

                //display user information on new post record section.
                try {
                    Picasso.get().load(userInformation?.defaultProfilePicture).into(image_user)
                    textview_user_name.text = userInformation?.fullName

                } catch (e: Exception) {
                    Log.d("homeFragment", "${e.message}")
                }


            }
        })

        val fbRef = FirebaseDatabase.getInstance().getReference("users")

        //get the post of friend from above friend list
        fbRef.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {

                adaptor.clear()

                userFriendList.forEach {
                    val dataForPostDisplay = p0.child(it).getValue(UserInformation::class.java)

                    if(dataForPostDisplay?.post!!.isNotEmpty()) {

                        dataForPostDisplay.post.forEach {post ->

                            adaptor.add(Status(dataForPostDisplay, post))
                        }
                    }
                }
            }
        })


        recyclerview_home.adapter = adaptor




        button_record.setOnClickListener {
            val dbRef = FirebaseDatabase.getInstance().getReference("/users/$currentUser")

            val status = edittext_status.text.toString()
            val date = Calendar.getInstance().time
            val formatter = SimpleDateFormat.getDateTimeInstance()
            val currentDate = formatter.format(date)


            if(status.isNotEmpty()) {
                val reference = dbRef.child("post/$currentDate")

                val post = Post(status)

                reference.setValue(post)
                    .addOnCompleteListener {
                        Toast.makeText(context, "status added", Toast.LENGTH_SHORT).show()
                        edittext_status.setText("")
                    }
            }
            else {
                Toast.makeText(context, "write some status.", Toast.LENGTH_SHORT).show()
            }

        }
    }
}