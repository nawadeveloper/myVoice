package np.com.nawarajbista.myvoice.ui.friends

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.AdapterListUpdateCallback
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import kotlinx.android.synthetic.main.fragment_friends.*
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import np.com.nawarajbista.myvoice.FriendSuggestion
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
        val adaptor = GroupAdapter<GroupieViewHolder>()
        adaptor.add(FriendSuggestion())
        recyclerview_friend_suggestion.adapter = adaptor
    }

}
