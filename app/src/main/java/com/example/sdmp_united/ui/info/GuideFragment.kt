package com.example.sdmp_united.ui.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.ImageSwitcher
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.sdmp_united.R
import kotlinx.android.synthetic.main.fragment_guide.*
import org.imaginativeworld.whynotimagecarousel.CarouselItem
import org.imaginativeworld.whynotimagecarousel.ImageCarousel


class GuideFragment : Fragment() {

    private lateinit var guideViewModel: GuideViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        guideViewModel =
                ViewModelProvider(this).get(GuideViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_guide, container, false)


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val carousel: ImageCarousel = carousel

        val list = mutableListOf<CarouselItem>()

        list.add(
                CarouselItem(
                        imageUrl = "https://i.ibb.co/1bZ4pg3/01.png",
                        caption = "Бокове меню"
                )
        )
        list.add(
                CarouselItem(
                        imageUrl = "https://i.ibb.co/10G1YT3/02.png",
                        caption = "Сторінка бази даних/опис можливих дій (з бокового меню)"
                )
        )
        list.add(
                CarouselItem(
                        imageUrl = "https://i.ibb.co/FKK5yxF/03.png",
                        caption = "Приклад діалогових вікон Add/Update/Details/Delete"
                )
        )
        list.add(
                CarouselItem(
                        imageUrl = "https://i.ibb.co/HBtPGT5/04.png",
                        caption = "Приклад побудови маршруту"
                )
        )
        list.add(
                CarouselItem(
                        imageUrl = "https://i.ibb.co/Kj0FD2W/05.png",
                        caption = "Приклад вибірки даних з адресної книги (з бокового меню)"
                )
        )
        list.add(
                CarouselItem(
                        imageUrl = "https://i.ibb.co/bdZDH5C/06.png",
                        caption = "Приклад вибірки з бази даних (з бокового меню)"
                )
        )
        list.add(
                CarouselItem(
                        imageUrl = "https://i.ibb.co/tpRVs7q/07.png",
                        caption = "Приклад сторінки (з бокового меню) з незакодованим текстом, щоб закодувати - натиснути Classic Huffman або Modified Huffman"
                )
        )
        list.add(
                CarouselItem(
                        imageUrl = "https://i.ibb.co/5Ft6Nvj/08.png",
                        caption = "Приклад сторінки після натискання однієї з кнопок Classic Huffman або Modified Huffman"
                )
        )
        list.add(
                CarouselItem(
                        imageUrl = "https://i.ibb.co/CWnPCkd/09.png",
                        caption = "Приклад діалогового вікна по натиску клавіші Frequencies"
                )
        )
        list.add(
                CarouselItem(
                        imageUrl = "https://i.ibb.co/0qQtCML/10.png",
                        caption = "Приклад діалогового вікна по натиску клавіші Codes"
                )
        )
        list.add(
                CarouselItem(
                        imageUrl = "https://i.ibb.co/dWWnWbr/11.png",
                        caption = "Приклад діалогового вікна по натиску клавіші Histogram"
                )
        )
        list.add(
                CarouselItem(
                        imageUrl = "https://i.ibb.co/CVgnBVd/12.png",
                        caption = "Приклад діалогового вікна по натиску клавіші Check"
                )
        )
        list.add(
                CarouselItem(
                        imageUrl = "https://i.ibb.co/NZkKC7W/13.png",
                        caption = "Приклад сповіщення, що файл збережено"
                )
        )
        list.add(
                CarouselItem(
                        imageUrl = "https://i.ibb.co/Mg8MBhD/14.png",
                        caption = "Сторінка автора (з бокового меню)"
                )
        )




        carousel.addData(list)
    }

}