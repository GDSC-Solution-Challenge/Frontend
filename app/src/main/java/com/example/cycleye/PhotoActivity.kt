package com.example.cycleye

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.method.ScrollingMovementMethod
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.cycleye.databinding.ActivityPhotoBinding
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat


class PhotoActivity : AppCompatActivity() {

    // constant
    private val PERMISSION_REQUEST = 100
    private val AUTHORITIES = "com.example.cycleye.fileprovider"

    //bind
    private val binding get() = photoBinding!!
    private var photoBinding : ActivityPhotoBinding? = null

    private lateinit var f: File;
    private var activityResultLauncher :  ActivityResultLauncher<Intent>? = null
    private var saveUri: Uri? = null

    // permissions
    val PERMISSIONS = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    //model
    private lateinit var C_classifier: Classifier
    private lateinit var OX_classifier: Classifier
    private var OX_result = ""
    private var type = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        photoBinding = ActivityPhotoBinding.inflate(layoutInflater)

        // launcher
        initClassifier()
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if(result.resultCode == Activity.RESULT_OK){
                BitmapFactory.decodeFile(f.absolutePath)?.let { image ->
                    binding.imageView.setImageBitmap(image)
                    val output1 = C_classifier.classify(image)
                    type = output1.first
                    val output2 = OX_classifier.classify(image)
                    if (output2.first == "O"){
                        OX_result = "Possible"
                    }
                    else if (output2.first == "X"){
                        OX_result = "Impossible"
                    }
                    else{
                        OX_result = "Vague"
                    }
                    val resultStr1 =
                        String.format("Type : %s", type)
                    val resultStr2 =
                        String.format("Separate collection : %s", OX_result)
                    binding.result1Text.text = resultStr1
                    binding.result2Text.text = resultStr2
                    binding.additionalMessage.movementMethod = ScrollingMovementMethod()
                    if (OX_result == "Vague" || OX_result == "Possible")
                    {
                        if (type == "Clothes"){
                            binding.additionalMessage.text = "Dispose of clothes in the dedicated collection box provided if there is a dedicated collection box for waste clothing. If not, remove foreign objects such as buttons, zippers, etc., and put them in a burlap bag or tie them up before disposing of them.\n" +
                                    "\n" +
                                    "Things to note are:\n" +
                                    " 1) We need to reduce the occurrence of old clothes by reducing the purchase of unnecessary clothes through planned purchases.\n" +
                                    " 2) Wash clothes that can be worn and exchange them with neighbors or relatives, or donate them to those in need.\n" +
                                    " 3) Clothes, blankets, cotton, pillows, carpets, leather products, disposable diapers, and waterproof coated clothes are excluded."
                        }
                        else if (type == "Electronic"){
                            binding.additionalMessage.text = "Large home appliances can be discharged independently, and a free on-site collection service is used. However, this applies only to home appliances, and business operators are excluded from the free service.\n" +
                                    "Small home appliances cannot be discharged alone, and when disposing of 5 or more items or subject items, collection is possible just like large home appliances.\n" +
                                    "Phone: 1599-0903, KakaoTalk ID: Free door-to-door collection of abandoned appliances, website: http://www.15990903.or.kr\n" +
                                    "\n" +
                                    "TIP: Donate Used Cell Phones\n" +
                                    "- Donated mobile phones are used as a fund for the underprivileged, and delivery by C.O.D. is also possible. Refer to the homepage (http://www.nanumphone.k-erc.kr)"
                        }
                        else if (type == "Furniture"){
                            binding.additionalMessage.text = "Since it is difficult to put furniture in bags, large waste stickers must be reported and pasted. \n " +
                                    "Stickers can be purchased by visiting the nearest community center or ward office, and you can report bulky waste on the online city/gun/gu office site, print them out, and attach the stickers to your furniture.\n" +
                                    "The price of the sticker ranges from 2,000 won to 10,000 won depending on the weight and size."
                        }
                        else if (type == "Glass"){
                            binding.additionalMessage.text = "Broken glass cannot be recycled. Therefore, it is sufficient to wrap the general volume-rate bag in newspaper so that it does not tear.\n" +
                                    "In the case of general glass, wrap it in newspaper so that it does not break and dispose of it as recycling.\n" +
                                    "Heat-resistant glass, used for microwave ovens or gas stoves, must be disposed of by purchasing volume-rate bags or special-sized sacks.\n" +
                                    "When recycling glass bottles, the contents must be removed, and glass bottles subject to a deposit for empty containers, such as soju and beer, can be returned to the retail store and the deposit refunded. At this time, the deposit can be refunded from 100 won to 130 won.\n" +
                                    "\n" +
                                    "Things to note are:\n" +
                                    "1) Milky white glass bottles are not recycled.\n" +
                                    "2) Mirrors, broken glass, ceramics, heat-resistant tableware, fluorescent lights, and light bulbs are not recycled.\n" +
                                    "3) After completely using the contents of the empty pesticide bottles, collect them separately in a sacking bag so that they are not mixed with other bottles and discharge them.\n" +
                                    "       (Collected by the Korea Resources Recycling Corporation after paying a deposit of 150 won per kg)"
                        }
                        else if (type == "Lightbulb"){
                            binding.additionalMessage.text = "In general, a dedicated collection box is used.\n" +
                                    "\n" +
                                    "Things to note are:\n" +
                                    "1) Waste fluorescent lamps contain harmful substances, so take care not to break them and dispose of them in the collection box dedicated to waste fluorescent lamps.\n" +
                                    "2) Broken waste fluorescent lamps should be wrapped with newspaper, etc. to prevent people from being stabbed or cut, and put them in a pay-as-you-go bag and dispose of them."
                        }
                        else if (type == "Metal"){
                            binding.additionalMessage.text = "Scrap metals such as tools, wires, and nails, and non-ferrous metals such as aluminum and stainless products are discharged so that foreign substances are not mixed.\n" +
                                    "\n" +
                                    "Things to note are:\n" +
                                    "1) If materials other than metal are attached, separate by material. At this time, if it is difficult to separate, it must be discharged according to the ordinance of the local government.\n" +
                                    "2) Hangers are disposed of as scrap metal.\n" +
                                    "3) Equipment (saw, hammer, sickle, etc.) is discharged as scrap metal. If separation is possible, separate by material. In other cases, refer to local government ordinances."
                        }
                        else if (type == "Paper"){
                            binding.additionalMessage.text = "paper\n" +
                                    "1) Newspapers are discharged by tying them with string\n" +
                                    "2) Booklets and notebooks are discharged after removing materials other than paper\n" +
                                    "3) Remove tapes from boxes and other items before discharging\n" +
                                    "4) Paper mixed with other materials (receipt, gold foil, wallpaper mixed with other materials), non-woven fabric, and plastic synthetic paper cannot be recycled.\n" +
                                    "\n" +
                                    "paper cups, packs\n" +
                                    "1) Paper cartons are discharged after removing materials other than paper cartons, washed, dried, and then discharged.\n" +
                                    "2) Rinse paper cups after removing foreign substances before disposing. However, if a large amount of food is on them, dispose of them as general trash.\n" +
                                    "\n" +
                                    "Things to note:\n" +
                                    "1) Coated paper cannot be recycled!!"
                        }
                        else if (type == "Plastic"){
                            binding.additionalMessage.text = "Containers and trays made of PET, PVC, PE, PP, PS, PSP, etc. are emptied and rinsed with water to remove foreign substances and discharged after removing the body and other materials.\n" +
                                    "\n" +
                                    "Things to note:\n" +
                                    "1) Toys, stationery, clothes hangers, toothbrushes, file folders, telephones, fishing rods, strollers, walkers, DVDs, CDs, etc., which are made of materials other than plastic, use pay-as-you-go bags and bulky waste disposal.\n" +
                                    "2) Pumps (perfume, shampoo) are separately discharged, and only the body is washed and discharged.\n" +
                                    "3) Toys are discharged as plastic, and if the material cannot be separated, it is reported as large waste.\n" +
                                    "4) Plastic integral instruments such as recorders are also discharged as plastic."
                        }
                        else if (type == "Pot"){
                            binding.additionalMessage.text = "In general, incombustible garbage is put in a special bag for separate collection.\n" +
                                    "\n" +
                                    "Things to note are:\n" +
                                    "1) If the pottery is broken\n" +
                                    "Seal it with paper such as newspaper so that it does not get hurt, put it in a non-combustible sacks and dispose of it in the collection area (recycle x) If it is difficult to obtain a non-combustible sacks, put the broken pottery in newspaper well and put the warning sign “Danger” or “Caution” in red After writing it down with a marker, put it in a regular volume-rate plastic bag and discharge it.\n" +
                                    "2) If the pottery is dirty -> After washing it well, put it in a sack exclusively for non-combustible garbage for separate collection (recycle o)\n" +
                                    "\n" +
                                    "Tip: It is called by various names such as non-combustible sacks, non-combustible volume-rate bags, non-combustible waste bags, and non-combustible waste sacks, and the price varies depending on the region. From 340 won to 3000 won."
                        }
                        else if (type == "Styrofoam"){
                            binding.additionalMessage.text = "Generally, it is separated and discharged to the Styro product part only when it is clean and there are no tape marks or foreign substances.\n" +
                                    "\n" +
                                    "Things to note are:\n" +
                                    "1) In case of foreign matter, it is a principle to wash thoroughly with water and separate discharge. However, if it is not cleaned well, put it in a pay-as-you-go bag and dispose of it with general garbage. (Principle = recycling o, if not wiped = recycling x)\n" +
                                    "2) If there are tapes, shipping invoices, or stickers attached to the box, remove them completely and throw them in general garbage (recycle x)\n" +
                                    "3) If there are colors and patterns used in places such as butcher shops, throw them away in general garbage (recycle x)\n" +
                                    "4) If it is used for individual packaging of fruits such as apples and pears, it is discarded in general garbage because the recycling value is low (recycling x)\n" +
                                    "5) Throw away fruit nets in general trash (recycle x)\n" +
                                    "\n" +
                                    "Tip: It's plastic after all, but it's recycled in a different way, so it's a separate discharge."
                        }
                        else if (type == "Vinyl"){
                            binding.additionalMessage.text = "In general, clean vinyl is separated and discharged into vinyl (recycling o)\n" +
                                    "\n" +
                                    "Things to note are:\n" +
                                    "1) In the case of a label attached to a PET bottle, the plastic bottle label is removed and collected separately. Remove only the label separately and separate it into vinyl (recycling o)\n" +
                                    "2) If stickers or tapes are attached, remove or cut them off and throw them away as general garbage, and then collect only the vinyl separately (recycle o)\n" +
                                    "3) If foreign matter is attached, wash it clean and collect it separately with vinyl. If it is difficult to remove foreign matter, discharge it as general garbage (recycling 0, removing foreign matter = recycling x)\n" +
                                    "4) Commercial wrap is discharged as general waste (recycling x)\n" +
                                    "5) Sausages wrapped in plastic: Dispose as general waste (recycle x)"
                        }
                        else if (type == "Wood"){
                            binding.additionalMessage.text = "Since trees cannot be recycled, small garbage is thrown away in a volume-rate bag, and bulky items such as furniture are reported as large household waste and discharged (recycling x)\n" +
                                    "\n" +
                                    "Things to note are:\n " +
                                    "If there are nails or sharp objects in the wood, remove them beforehand.\n" +
                                    "\n" +
                                    "Tip: Large-size waste stickers are priced according to their size, ranging from as little as 2,000 won to as large as 10,000 won. You can purchase it by visiting the nearest community center or ward office. In other cases, you can report bulky waste on the online city/gun/gu office site, print it out, and attach it."
                        }
                    }
                    else{
                        binding.additionalMessage.text = "Cannot be recycle. Please check again. \n " +
                                "\n" +
                                "In many cases, it could be due to poor quality of picture or not being separated from other kinds of things."
                    }
                }
            }
        }

        // Camera Btn Click
        binding.cameraBtn.setOnClickListener{
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val photoFile = File(
                File("${filesDir}/image").apply {
                    if(!this.exists()){
                        this.mkdirs()
                    }
                }, makeFileName()
            )
            saveUri = FileProvider.getUriForFile(
                this,
                AUTHORITIES,
                photoFile
            )
            intent.putExtra(MediaStore.EXTRA_OUTPUT, saveUri)
            f = photoFile
            activityResultLauncher!!.launch(intent)
        }

        setContentView(binding.root)
        checkPermissons(PERMISSIONS, PERMISSION_REQUEST)
    }

    private fun checkPermissons(permissions: Array<String>, permissionsRequest: Int): Boolean {
        val permissionList: MutableList<String> = mutableListOf()
        for(permission in permissions) {
            val result = ContextCompat.checkSelfPermission(this, permission)
            if(result != PackageManager.PERMISSION_GRANTED){
                permissionList.add(permission)
            }
        }
        if(permissionList.isNotEmpty()){
            ActivityCompat.requestPermissions(this, permissionList.toTypedArray(), PERMISSION_REQUEST)
            return false
        }
        return true
    }

    //카메라 권한 검사 후 callback 함수
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        for(result in grantResults){
            if(result != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "권한 승인이 필요합니다.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun makeFileName() : String {
        val sdf = SimpleDateFormat("yyyyMMdd_HHmmss")
        val filename = sdf.format(System.currentTimeMillis())
        return "${filename}.jpg"
    }

    private fun initClassifier() {
        C_classifier = Classifier(this, Classifier.CLASSIFY_MODEL)
        OX_classifier = Classifier(this, Classifier.OX_MODEL)
        try {
            C_classifier.init("models/classification_labels.txt")
            OX_classifier.init("models/OX_labels.txt")
        } catch (exception: IOException) {
            Toast.makeText(this, "Can not init Classifier!!", Toast.LENGTH_SHORT).show()
        }
    }
}