package np.com.nawarajbista.myvoice.ui.friends

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import kotlinx.android.synthetic.main.fragment_friends.*
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import np.com.nawarajbista.myvoice.*
import np.com.nawarajbista.myvoice.R

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
        val suggestionAdaptor = GroupAdapter<GroupieViewHolder>()
        val requestAdaptor = GroupAdapter<GroupieViewHolder>()



        val ref = FirebaseDatabase.getInstance().getReference("/users")
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser?.uid
        val alreadyRequested = arrayListOf<String?>()
        val requestReceivedFrom = arrayListOf<String?>()


        ref.child("$currentUser").addValueEventListener(object : ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(context, p0.message, Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(p0: DataSnapshot) {

                alreadyRequested.clear()
                requestReceivedFrom.clear()


                val removeFromSuggestionList = p0.getValue(AlreadyRequested::class.java)

                if (removeFromSuggestionList != null) {
                    val data = removeFromSuggestionList.requestSendTo.values

                    alreadyRequested.addAll(data)
                }


                //second method of doing it without making another class
                p0.child("requestReceivedFrom").children.forEach {
                    alreadyRequested.add("${it.value}")
                    requestReceivedFrom.add("${it.value}")
                }

                p0.child("friendList").children.forEach {
                    alreadyRequested.add("${it.key}")
                }

            }
        })




        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(context, p0.message, Toast.LENGTH_SHORT).show()

            }

            override fun onDataChange(p0: DataSnapshot) {
                requestAdaptor.clear()
                suggestionAdaptor.clear()

                for (snap in p0.children) {

                    val friendSuggestion = snap.getValue(UserDataFireBase::class.java)



                    if (currentUser != snap.key) {

                        if (snap.key !in alreadyRequested) {
                            suggestionAdaptor.add(
                                FriendSuggestion(
                                    friendSuggestion,
                                    snap.key.toString()
                                )
                            )
                        }

                        if(snap.key in requestReceivedFrom) {
                            requestAdaptor.add(
                                FriendRequest(friendSuggestion, snap.key.toString())
                            )
                        }
                    }
                }

                if(suggestionAdaptor.itemCount != 0) {
                    if(textview_request_title != null) {

                        textview_suggestion_title.visibility = View.VISIBLE
                    }
                }
                else {
                    if(textview_request_title != null) {

                        textview_suggestion_title.visibility = View.GONE
                    }
                }

                if(requestAdaptor.itemCount != 0) {
                    if(textview_request_title != null) {

                        textview_request_title.visibility = View.VISIBLE
                    }
                }
                else {
                    if(textview_request_title != null) {

                        textview_request_title.visibility = View.GONE
                    }
                }
            }
        })


        recyclerview_friend_request.adapter = requestAdaptor
        recyclerview_friend_suggestion.adapter = suggestionAdaptor

    }
}
