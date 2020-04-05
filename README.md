# IF2210 Project Template

## NOTES
* Views (file fxml) ada di resources/card/views

SOLVED
* Path img di csv beda sama di directory
* Ada conflict air/water temple
* Ada beberapa tab di string deskripsi kartu, jadi data yang ke parse sempet kaco (aku ga tau ini salah dari sana atau pas aku convert indentation)
* Foto Iroh.png gak ada, sempet kaco juga :(

ONGOING
* Card.fxml masih sangat poop, sementara masih buat testing aja
* Sekarang lagi nyobain Card.fxml bisa import Attribute.fxml yang beda-beda (jd layoutnya tergantung jenis kartu)
* yg jago front end help bikinin yg bagus dan bisa resize dong :(, thenks
* Gimana cara communicate between controller? kalo punya instance controller di dalam controller sepertinya strong coupling...
* Bikin Builder Class buat Card

Good read: http://griffon-framework.org/tutorials/5_mvc_patterns.html

## Running
Here is an example of project using gradle as the build tools.
Try running these commands:

`./gradlew run`

You will notice that it will open a window that display 'Avatar Duel'.
In the command line you can see the data that is being read by `CSVReader.java`

What happen is when you use `./gradlew run`, it will start the main function in your app.
For this app, the main function lives in `AvatarDuel.java`.

You can explore more about gradle [here](https://guides.gradle.org/creating-new-gradle-builds/)

## Credit

All images and description are taken from [Avatar Wikia](https://avatar.fandom.com/wiki/Avatar_Wiki)
