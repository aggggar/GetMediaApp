package com.example.getmediaapp.ui.get_cont

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.example.getmediaapp.R
import kotlinx.android.synthetic.main.fragment_set_count_dialog.*

/**
 * A simple [Fragment] subclass.
 */
class SetCountDialogFragment : DialogFragment() {

    private lateinit var setCount: SetCount


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_set_count_dialog, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is SetCount) {
            setCount = context
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnSet.setOnClickListener {
            if (!TextUtils.isEmpty(etCount.text)){
                setCount.setCount(etCount.text.toString())
                dismiss()
            }
        }
    }

    interface SetCount{
        fun setCount(count: String)
    }
}
