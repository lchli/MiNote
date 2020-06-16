package com.lch.menote.user.ui

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.blankj.utilcode.util.ToastUtils
import com.lch.menote.R
import com.lch.menote.user.presenterx.RegisterPresenter
import com.lch.menote.user.vm.RegisterVM
import com.lchli.utils.tool.VF
import com.lchli.utils.widget.CommonTitleView
import java.lang.ref.WeakReference


/**
 * Created by lchli on 2016/8/10.
 */
class RegisterFragment : Fragment(), RegisterPresenter.MvpView {

    private var common_title: CommonTitleView? = null
    private var register_widget: View? = null

    private var mRegisterViewModel: RegisterPresenter? = null
    private var mLoadingDialog: ProgressDialog? = null
    private var user_account_edit: EditText? = null
    private var user_pwd_edit: EditText? = null
    private val viewModel: RegisterVM = ViewModelProviders.of(this).get(RegisterVM::class.java)
     //val  viewModel: RegisterVM by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mLoadingDialog = ProgressDialog(activity)
        mRegisterViewModel = RegisterPresenter(ViewProxy(this))
        viewModel.launchDestination.observe(this, Observer<String> { dest->{
            //when(dest)

        }})


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        user_account_edit = VF.f<EditText>(view, R.id.user_account_edit)
        user_pwd_edit = VF.f<EditText>(view, R.id.user_pwd_edit)
        common_title = VF.f<CommonTitleView>(view, R.id.common_title)
        register_widget = VF.f<View>(view, R.id.register_widget)

        common_title!!.addRightText(getString(R.string.user_goto_login_text)) {
            val userFragmentContainer = parentFragment as UserFragmentContainer?
            userFragmentContainer!!.toLogin(true)
        }
        common_title!!.setCenterText("", null)
        common_title!!.addLeftIcon(R.drawable.arrow_left_back) {
            val userFragmentContainer = parentFragment as UserFragmentContainer?
            userFragmentContainer!!.toUserCenter(true)
        }
        register_widget!!.setOnClickListener {
            mRegisterViewModel!!.register(user_account_edit!!.text.toString(), user_pwd_edit!!.text.toString(), null)

        }

    }


    override fun showLoading() {
        mLoadingDialog!!.show()
    }

    override fun showFail(msg: String) {
        ToastUtils.showLong(msg)
    }

    override fun toUserCenter() {
        val p = parentFragment as UserFragmentContainer?
        p!!.toUserCenter(false)
    }

    override fun dismissLoading() {
        mLoadingDialog!!.dismiss()
    }


     class ViewProxy  constructor(activity: RegisterPresenter.MvpView) : RegisterPresenter.MvpView {

        private val uiRef= WeakReference<RegisterPresenter.MvpView>(activity)

         override fun showLoading() {
            val ui = uiRef.get()
            ui?.showLoading()
        }

        override fun dismissLoading() {
            val ui = uiRef.get()
            ui?.dismissLoading()
        }

        override fun toUserCenter() {
            val ui = uiRef.get()
            ui?.toUserCenter()
        }

        override fun showFail(msg: String) {
            val ui = uiRef.get()
            ui?.showFail(msg)
        }

    }
}