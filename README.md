# Avatar Duel
Card game sederhana yang memanfaatkan konsep OOP dalam implementasinya.
Proyek ini merupakan tugas besar 2 mata kuliah IF2210-Pemrograman Berorientasi Objek 2019/2020.

## Gameplay
Berikut adalah beberapa aturan bermain
* 1.Permainan dimainkan oleh dua orang secara bergantian yang terdiri atas Draw phase, Main Phase, Battle Phase, dan End Phase.
* 2.Setiap pemain akan diberikan deck kartu dan memiliki 80 HP.
* 3.Kartu yang diberikan dapat terdiri atas kartu karakter, kartu land, dan kartu skill. Tiap kartu bisa merupakan satu *dari 5 elemen yang ada yaitu earth, air,water, fire, *energy.
* 3.Pada awal game, tiap pemain mengambil 7 kartu dari deck ketangan.
* 4.Pada Draw phase , Pemain akan mendapatkan satu kartu dari deck milik pemain tersebut ke tangan
* 5.Saat Main Phase, pemain dapat meletakan beberapa kartu karakterdalam posisi bertarung atau bertahan, mengubah posisi kartu di field, meletakan maksimal 1 kartu land, meletakkan kartu skill serta karakter yang dikenai skill tersebut, dan membuang kartu skill
* 6.Battle Phase berlangsung dapat menggunakan karakter untuk menyerang lawan,tidak bisa menyerang HP lawan secara langsung.Setiap karakter hanya dapat menyerang maksimal satu kali.
* 7. End Phase Pemain mengakhiri giliran pada phase ini. Lawan akan memulai giliran-nya, dimulai dari draw phase, dan seterusnya.
* 8. Perlu diingat ketika endphase bisa jadi kartu ditangan penuh. Double click untuk discard sebuah kartu

![screen_1](some screenshot)

![screen_2](maybe a gif showcasing the gameplay!)

## Prerequisites

Berikut adalah keperluan untuk menjalankan game ini
* Java 8
* Gradle

## Running

Untuk menjalankan permainan, jalankan perintah berikut
```
gradlew run
```

## Unit Testing

hmm gmn ya


## Documentation
Dokumentasi dapat diakses di build/docs/javadoc/index.html, dokumentasi ini dibuat menggunakan javadoc.

## Project Structure
Berikut adalah struktur folder dari proyek ini
```
+---bin
+---deploy
+---docs
+---shipshopbipbop
```
(tinggal ```tree -f``` terus copas kalo dah fix.)

## Built With

* [Java](https://www.java.com/en/) - Bahasa pemrograman
* [Gradle](https://gradle.org/) - Build tool

## Authors
* **Arung Agamani Budi Putera** - *13518005*
* **Faris Fadhilah** - *13518026*
* **Faris Rizki Ekananda** - *13518125*
* **Michel Fang** - *13518137*
* **Yasyfiana Fariha Putrisusari** - *13518143*


## Acknowledgments

* Dosen IF2210 K2, Muhammad Zuhri Catur Candra, ST., MT
* Asisten Pembimbing, Antonio Setya

## Notes
Fitur-fitur dasar dan tambahan tidak terlepas dari bug, terutama fitur-fitur yang merupakan spesifikasi *bonus*, oleh karena itu kami minta maaf atas ketidaksempurnaan program.

## Credit
All card images and description are taken from [Avatar Wikia](https://avatar.fandom.com/wiki/Avatar_Wiki)