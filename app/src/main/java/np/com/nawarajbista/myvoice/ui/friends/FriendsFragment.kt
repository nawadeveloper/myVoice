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
import kotlinx.coroutines.NonCancellable.children
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
        val alreadyRequested = arrayListOf<String>()


        ref.child("$currentUser").addChildEventListener(object: ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                TODO("start from here")
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }
        })


        ref.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {

                for (snap in p0.children) {

                    val friendSuggestion = snap.getValue(UserDataFireBase::class.java)


                    if(currentUser == snap.key) {
                        snap.child("requestSendTo").children.forEach {
                            alreadyRequested.add("${it.value}")
                        }
                        snap.child("requestReceivedFrom").children.forEach {
                            alreadyRequested.add("${it.value}")
                        }

                        TODO("need to work on this")
                    }


                    if(currentUser != snap.key || snap.key in alreadyRequested) {
                        adaptor.add(FriendSuggestion(
                            friendSuggestion?.fullName.toString(),
                            friendSuggestion?.defaultProfilePicture.toString(),
                            snap.key.toString()
                        ))
                    }

                }
            }
        })

        recyclerview_friend_suggestion.adapter = adaptor
    }

}
