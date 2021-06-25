package com.toeker.quizgame.ui.fragments

import android.app.Dialog
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.OnProgressListener
import com.google.firebase.storage.StorageReference
import com.toeker.quizgame.R
import com.toeker.quizgame.data.models.QuestionModel
import com.toeker.quizgame.data.models.QuizModel2
import com.toeker.quizgame.ui.adapters.QuestionAdapter
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.android.synthetic.main.add_question.*
import kotlinx.android.synthetic.main.fragment_create_quiz.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlin.random.Random

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CreateQuizFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CreateQuizFragment : Fragment() , AdapterView.OnItemSelectedListener {
    private var spinner: Spinner? = null
    private var arrayAdapter: ArrayAdapter<String>? = null

    private var questionModelList: ArrayList<QuestionModel> = ArrayList()

    private lateinit var questionAdapter: QuestionAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager

    private var storeReference: StorageReference = FirebaseStorage.getInstance().getReference()
    private var imageUri: Uri? = null

    private val database by lazy{
        FirebaseFirestore.getInstance()
    }
    private val auth by lazy {
        FirebaseAuth.getInstance()
    }

    private lateinit var imageUrl: String
    lateinit var title: String
    var author: String = ""
    var numberOfQue: Int = 0
    var userID: String = ""
    var visible: Boolean = false
    var pin: Int = 0
    lateinit var description: String
    var visibility: Boolean = true
    lateinit var questions: ArrayList<QuestionModel>

    var currentUser = auth.currentUser
    var uid: String = currentUser!!.uid

    var reference: DocumentReference = database.collection("users").document(uid)

    private var itemList = arrayOf(
        "Herkes",
        "Gizli"
    )




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        createQuizProgress.visibility = View.INVISIBLE

        reference.get()
                .addOnCompleteListener(OnCompleteListener {
                    if (it.getResult()?.exists() == true) {

                        var name: String? = it.getResult()!!.getString("userName")

                        author = name.toString()
                        userID = uid

                    } else {

                    }
                })

        /*activity?.let{
            val intent = Intent (it, CreateQuizActivity::class.java)
            it.startActivity(intent)
        }*/

        linearLayoutManager = LinearLayoutManager(context)
        recycSorular.layoutManager = linearLayoutManager

        spinner = view.findViewById(R.id.spinner)
        arrayAdapter = context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item, itemList) }
        spinner?.adapter = arrayAdapter
        spinner?.onItemSelectedListener = this

        quizImage.setOnClickListener(View.OnClickListener {

            val galleryIntent = Intent(Intent.ACTION_GET_CONTENT)
            galleryIntent.setType("image/*")
            startActivityForResult(galleryIntent,2)

        })

        btnSoruEkle.setOnClickListener(View.OnClickListener {
            showDialog()
        })

        btnKaydet.setOnClickListener(View.OnClickListener {

            if(imageUri != null){

                reference.update("createdQuiz", FieldValue.increment(1))
                uploadToFirebase(imageUri!!)
            }
            else{
                Toast.makeText(context,"Lütfen bir resim ekleyin",Toast.LENGTH_SHORT).show()
            }


        })

    }

    fun uploadToFirebase(uri: Uri){
        var fileRef: StorageReference = storeReference.child(System.currentTimeMillis().toString() +
                "." + getFileExtension(uri))
        fileRef.putFile(uri).addOnSuccessListener(OnSuccessListener {
            fileRef.downloadUrl.addOnSuccessListener {
                imageUrl = it.toString()
                saveDataToFirebase()
                createQuizProgress.visibility = View.INVISIBLE
            }
        }).addOnProgressListener {
            createQuizProgress.visibility = View.VISIBLE
        }
            .addOnFailureListener {
                createQuizProgress.visibility = View.INVISIBLE
                Toast.makeText(context,"Kayıt Başarısız",Toast.LENGTH_SHORT).show()
            }
    }
    fun getFileExtension(mUri: Uri): String? {

        var cr: ContentResolver
        cr = requireContext().getContentResolver()
        var mime: MimeTypeMap = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cr.getType(mUri))
    }
    fun saveDataToFirebase(){
        title = editTxtTitle.text.toString()
        description = editTxtDesc.text.toString()
        numberOfQue = questionModelList.size
        var quiz: QuizModel2 = QuizModel2(userID,author,title,numberOfQue,description
            ,visibility,pin,imageUrl,questionModelList)
        database
            .collection("quiz")
            .add(quiz).addOnCompleteListener(OnCompleteListener {
                if(it.isSuccessful){
                    Toast.makeText(this@CreateQuizFragment.context, "Kayıt Başarılı", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this@CreateQuizFragment.context, "Kayıt Başarısız", Toast.LENGTH_SHORT).show()
                }
            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 2 && resultCode == -1 && data != null){
            imageUri = data.data!!
            quizImage.setImageURI(imageUri)

        }
    }

     fun showDialog() {
        val dialog = activity?.let { Dialog(it) }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
         if (dialog?.getWindow() != null) {
             val colorDrawable = ColorDrawable(Color.TRANSPARENT)
             dialog.getWindow()!!.setBackgroundDrawable(colorDrawable)
         }
        dialog?.setCancelable(false)
        dialog?.setContentView(R.layout.add_question)
        dialog?.show()

         lateinit var btnClose: Button
         lateinit var btnEkle: Button
         lateinit var editTextSoru: EditText
         lateinit var optA: EditText
         lateinit var optB: EditText
         lateinit var optC: EditText
         lateinit var optD: EditText
         lateinit var chckBox: RadioButton
         lateinit var chckBox1: RadioButton
         lateinit var chckBox2: RadioButton
         lateinit var chckBox3: RadioButton
         var answer: String? = null

         editTextSoru = dialog?.findViewById(R.id.editTextSoru)!!
         optA = dialog?.findViewById(R.id.optA)!!
         optB = dialog?.findViewById(R.id.optB)!!
         optC = dialog?.findViewById(R.id.optC)!!
         optD = dialog?.findViewById(R.id.optD)!!

         chckBox = dialog?.findViewById(R.id.checkBox)!!
         chckBox1 = dialog?.findViewById(R.id.checkBox2)!!
         chckBox2 = dialog?.findViewById(R.id.checkBox3)!!
         chckBox3 = dialog?.findViewById(R.id.checkBox4)!!

         btnEkle = dialog?.findViewById(R.id.btnEkle)!!

         btnClose = dialog?.findViewById<Button>(R.id.btnKapat)!!

         chckBox.setOnClickListener(View.OnClickListener{
             chckBox1.isChecked = false
             chckBox2.isChecked = false
             chckBox3.isChecked = false
             answer = optA.text.toString()
         })
         chckBox1.setOnClickListener(View.OnClickListener{
             chckBox.isChecked = false
             chckBox2.isChecked = false
             chckBox3.isChecked = false
             answer = optB.text.toString()
         })
         chckBox2.setOnClickListener(View.OnClickListener{
             chckBox1.isChecked = false
             chckBox.isChecked = false
             chckBox3.isChecked = false
             answer = optC.text.toString()
         })
         chckBox3.setOnClickListener(View.OnClickListener{
             chckBox1.isChecked = false
             chckBox2.isChecked = false
             chckBox.isChecked = false
             answer = optD.text.toString()
         })

         btnEkle.setOnClickListener(View.OnClickListener {
             if(chckBox.isChecked || chckBox1.isChecked ||chckBox2.isChecked ||chckBox3.isChecked){
                 questionModelList.add(QuestionModel(
                         editTextSoru.text.toString(),
                         optA.text.toString(),
                         optB.text.toString(),
                         optC.text.toString(),
                         optD.text.toString(),
                         answer
                 ))
                 loadCards()
                 dialog?.dismiss()
             }
             else{
                 Toast.makeText(context,"Lüfen doğru seçeneği işaretleyin!!",Toast.LENGTH_SHORT).show()
                 chckBox.isChecked = true
                 chckBox1.isChecked = true
                 chckBox2.isChecked = true
                 chckBox3.isChecked = true
                 Thread.sleep(200)
                 chckBox.isChecked = false
                 chckBox1.isChecked = false
                 chckBox2.isChecked = false
                 chckBox3.isChecked = false
             }


         })

         btnClose.setOnClickListener(View.OnClickListener {
             dialog?.dismiss()
         })


    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_quiz, container, false)

    }

    private fun loadCards() {

        //init list
        //setup adapter

        questionAdapter = activity?.let { QuestionAdapter(it,questionModelList) }!!

        //set adapter to recycleview
        recycSorular.setHasFixedSize(true)
        recycSorular.adapter = this.questionAdapter

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CreateQuizFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CreateQuizFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        var items:String = parent?.getItemAtPosition(position) as String
        if(items == "Herkes"){
            txtPin.visibility = View.INVISIBLE
            visibility = true;
        }
        else{
            val randomm = (100000..999999).random()
            pin = randomm
            txtPin.visibility = View.VISIBLE
            visibility = false
            txtPin.setText("PIN: ${pin}")
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}