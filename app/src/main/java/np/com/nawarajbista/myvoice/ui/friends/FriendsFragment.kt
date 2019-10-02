package np.com.nawarajbista.myvoice.ui.friends

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import kotlinx.android.synthetic.main.fragment_friends.*
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import np.com.nawarajbista.myvoice.AlreadyRequested
import np.com.nawarajbista.myvoice.FriendSuggestion
import np.com.nawarajbista.myvoice.R
import np.com.nawarajbista.myvoice.UserDataFireBase

class FriendsFragment : Fragment() {


    private lateinit var friendsViewModel: FriendsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        friendsViewModel =
            ViewModelProviders.of(this).get(FriendsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_friends, container, false)
        //val textView: TextView = root.findViewById(R.id.friends)
        friendsViewModel.text.observe(this, Observer {
            //textView.text = it
        })

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adaptor = GroupAdapter<GroupieViewHolder>()

        val ref = FirebaseDatabase.getInstance().getReference("/users")
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser?.uid
        val alreadyRequested = arrayListOf<String?>()


        ref.child("$currentUser").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {

                val removeFromSuggestionList = p0.getValue(AlreadyRequested::class.java)

                if (removeFromSuggestionList != null) {
                    val data = removeFromSuggestionList.requestSendTo.values

                    alreadyRequested.addAll(data)
                }


                //second method of doing it without making another class
                p0.child("requestReceivedFrom").children.forEach {

//                    Log.d("FriendsFragment", "${it.value}")
                    alreadyRequested.add("${it.value}")
                }

            }
        })


        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {

                adaptor.clear()

                for (snap in p0.children) {

                    val friendSuggestion = snap.getValue(UserDataFireBase::class.java)



                    if (currentUser != snap.key) {

                        if(snap.key !in alreadyRequested)
                        adaptor.add(
                            FriendSuggestion(
                                friendSuggestion?.fullName.toString(),
                                friendSuggestion?.defaultProfilePicture.toString(),
                                snap.key.toString()
                            )
                        )
                    }

                }
            }
        })

        recyclerview_friend_suggestion.adapter = adaptor
    }

}
