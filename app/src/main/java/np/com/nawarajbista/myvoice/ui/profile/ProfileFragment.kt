package np.com.nawarajbista.myvoice.ui.profile

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*
import np.com.nawarajbista.myvoice.MainActivity
import np.com.nawarajbista.myvoice.R
import np.com.nawarajbista.myvoice.SignInActivity
import np.com.nawarajbista.myvoice.UserDataFireBase



class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel
    lateinit var auth: FirebaseAuth
    private lateinit var ref: DatabaseReference
    private var selectedPhotoURI: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        profileViewModel =
            ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_profile, container, false)
        //val textView: TextView = root.findViewById(R.id.profile)
        profileViewModel.text.observe(this, Observer {
            //textView.text = it
        })

        val myActivity = activity as MainActivity
        ref = myActivity.getUserData()


        ref.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val user = dataSnapshot.getValue(UserDataFireBase::class.java)
                Picasso.get().load(user?.defaultProfilePicture).into(profile_image)
                user_name.text = user?.fullName
                user_email.text = user?.email

            }
        })

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        sign_out.setOnClickListener {

            auth.signOut()

            val intent = Intent(context, SignInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        change_image.setOnClickListener {
            getImageFromDevice()
        }

        edit_user_name.setOnClickListener {
            user_name.visibility = View.GONE
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {

            selectedPhotoURI = data.data

            val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, selectedPhotoURI)
            profile_image.setImageBitmap(bitmap)

            saveImageToStorage()


        }
    }

    private fun getImageFromDevice() {

        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 0)
    }

    private fun saveImageToStorage() {


        if(selectedPhotoURI == null) {
            Log.d("profileFragment", "selectedPhotoURI is null")
            return
        }

        val userID = FirebaseAuth.getInstance().currentUser?.uid
        val storageRef = FirebaseStorage.getInstance().getReference("/user_profile_image/$userID")

        storageRef.putFile(selectedPhotoURI!!)
            .addOnSuccessListener {

                storageRef.downloadUrl.addOnSuccessListener {
                    updateImageToDatabase(it.toString())
                }

            }
            .addOnFailureListener {
                Log.d("ProfileFragment", "${it.message}")
            }


    }


    private fun updateImageToDatabase(imagePath: String) {
        val data = mapOf("defaultProfilePicture" to imagePath)
        ref.updateChildren(data)
            .addOnSuccessListener {
                Log.d("ProfileFragment", "New Image Updated Successfully")
            }
            .addOnFailureListener {
                Log.d("ProfileFragment", "${it.message}")
            }
    }



}