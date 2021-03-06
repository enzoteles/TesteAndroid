package br.com.santander.desafio.detail

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.enzoteles.quickhelp.fragment.HelpFragment
import br.com.santander.desafio.Constants
import br.com.santander.desafio.R
import br.com.santander.desafio.detail.adapter.DownInfoListAdapter
import br.com.santander.desafio.detail.adapter.InfoListAdapter
import br.com.santander.desafio.main.MainActivity
import br.com.santander.desafio.webservice.fund.DownInfoItem
import br.com.santander.desafio.webservice.fund.InfoItem
import br.com.santander.desafio.webservice.fund.ResponseFund
import kotlinx.android.synthetic.main.component_risk.*
import kotlinx.android.synthetic.main.detail.*
import kotlinx.android.synthetic.main.item_more_info.*
import kotlinx.android.synthetic.main.toolbar_details.*
import kotlinx.android.synthetic.main.toolbar_details.view.*
import javax.inject.Inject

class DetailFragment: HelpFragment(),DetailMVP.View{

    @Inject
    lateinit var presenter: DetailMVP.Presenter
    lateinit var adapterInfo: InfoListAdapter
    lateinit var adapterDonwInfo: DownInfoListAdapter
    lateinit var layoutManager: LinearLayoutManager
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {

        val view = inflater!!.inflate(R.layout.detail, container, false)
        return view
    }

    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initInject()
        initUI()
        initDate()
    }

    override fun initInject() {
        val detailComponent = DaggerDetailComponent.builder()
                .detailModule(DetailModule(this))
                .build()
        detailComponent.inject(this)
    }

    override fun initUI(){

        var investimento = getString(R.string.lg_bt_investment)
        toolbar_detail.dt_tb_tv_title.setText(investimento)
        avi_detail.show()
        presenter.initInteractor()
    }

    override fun initDate() {
        presenter.getFunc()?.observe(activity as MainActivity, object : Observer<ResponseFund> {
            override fun onChanged(@Nullable response: ResponseFund?) {
                if (response != null) {
                    setDataView(response)
                }
            }
        })
    }

    override fun setDataView(response: ResponseFund) {

        avi_detail.hide()
        dt_ll_details.visibility = View.VISIBLE

        var listInfo:MutableList<InfoItem> = arrayListOf()
        var listDownInfo:MutableList<DownInfoItem> = arrayListOf()

        verificationRisk(response!!.screen!!.risk)
        response.screen!!.info!!.forEach {
            it->
            if (it != null) {
                listInfo.add(it)
            }
        }

        response.screen!!.downInfo!!.forEach {
            down->
            if (down != null) {
                listDownInfo.add(down)
            }
        }

        setRecyclerviewInfo(listInfo)
        setRecyclerviewDownInfo(listDownInfo)

        dt_tv_title.setText(response!!.screen!!.title)
        dt_tv_fund_name.setText(response!!.screen!!.fundName)
        dt_tv_what.setText(response!!.screen!!.whatIs)
        dt_tv_definition.setText(response!!.screen!!.definition)
        dt_tv_risk_title.setText(response!!.screen!!.riskTitle)
        dt_tv_info_title.setText(response!!.screen!!.infoTitle)

        dt_mounth_tv_found.setText("${response!!.screen!!.moreInfo!!.month!!.fund}%")
        dt_mounth_tv_cdi.setText("${response!!.screen!!.moreInfo!!.month!!.cDI}%")

        dt_year_tv_found.setText("${response!!.screen!!.moreInfo!!.year!!.fund}%")
        dt_year_tv_cdi.setText("${response!!.screen!!.moreInfo!!.year!!.cDI}%")

        dt_12m_tv_found.setText("${response!!.screen!!.moreInfo!!.jsonMember12months!!.fund}%")
        dt_12m_tv_cdi.setText("${response!!.screen!!.moreInfo!!.jsonMember12months!!.cDI}%")

    }

    override fun setRecyclerviewInfo(listInfo: MutableList<InfoItem>) {

        layoutManager = LinearLayoutManager(Constants.context, LinearLayoutManager.VERTICAL, false)
        //list info
        dt_rv_info.layoutManager = layoutManager
        dt_rv_info.setHasFixedSize(true)
        adapterInfo = InfoListAdapter(listInfo, Constants.context!!)
        dt_rv_info.adapter = adapterInfo

    }

    override fun setRecyclerviewDownInfo(listDownInfo: MutableList<DownInfoItem>) {
        layoutManager = LinearLayoutManager(Constants.context, LinearLayoutManager.VERTICAL, false)
        //list download info
        dt_rv_down_info.layoutManager = layoutManager
        dt_rv_down_info.setHasFixedSize(true)
        adapterDonwInfo = DownInfoListAdapter(listDownInfo, Constants.context!!)
        dt_rv_down_info.adapter = adapterDonwInfo
    }

    override fun verificationRisk(risk: Int?) {

        when(risk) {
            1 ->{
                img_one.visibility = View.VISIBLE
                view_one_one.visibility = View.VISIBLE
            }
            2 ->{
                img_two.visibility = View.VISIBLE
                view_two_one.visibility = View.VISIBLE
            }
            3 ->{
                img_three.visibility = View.VISIBLE
                view_three_one.visibility = View.VISIBLE
            }
            4 ->{
                img_four.visibility = View.VISIBLE
                view_four_one.visibility = View.VISIBLE
            }
            5 ->{
                img_five.visibility = View.VISIBLE
                view_five_one.visibility = View.VISIBLE
            }

        }

    }


}