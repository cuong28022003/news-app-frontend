package com.hoangtien2k3.news_app.ui.football.adapter

import android.annotation.SuppressLint
import com.hoangtien2k3.news_app.data.models.Football

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hoangtien2k3.news_app.databinding.ItemNewsBinding
import com.squareup.picasso.Picasso
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class FoolballAdapter(
    private val mCtx: Context,
    private var heroList: List<Football>,
    private val showDialoginterface: ShowDialoginterface
) : RecyclerView.Adapter<FoolballAdapter.HeroViewHolder>() {

    interface ShowDialoginterface {
        fun itemClik(hero: Football)
    }

    inner class HeroViewHolder(private val binding: ItemNewsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(hero: Football) {
            binding.title.ellipsize = TextUtils.TruncateAt.MARQUEE
            binding.title.isSelected = true
            binding.title.isSingleLine = true

            Picasso.get().load(hero.thumbnail).into(binding.image)
            binding.title.text = hero.title

            val data = fromISO8601UTC(hero.date)
            val dateFormat = SimpleDateFormat("yyyy-MM-dd, HH:mm")

            if (data != null) {
                val strDate = dateFormat.format(data)
                binding.date.text = strDate
                println(hero.date)
            } else {
                println(hero.date)
            }

            binding.root.setOnClickListener {
                showDialoginterface.itemClik(hero)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeroViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HeroViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HeroViewHolder, position: Int) {
        holder.bind(heroList[position])
    }

    // Update data function
    fun updateData(newList: List<Football>) {
        heroList = newList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return heroList.size
    }

    @SuppressLint("SimpleDateFormat")
    private fun fromISO8601UTC(dateStr: String): Date? {
        val tz = TimeZone.getTimeZone("UTC")
        val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
        df.timeZone = tz

        try {
            return df.parse(dateStr)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return null
    }
}