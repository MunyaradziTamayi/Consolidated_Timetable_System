package com.timetable.timetable.helpers

import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths

object MultiPartFileToFileConverter {
    @Throws(IOException::class)
    fun convert(file: MultipartFile): File {
        val mFile = File(System.getProperty("user.dir") + "/Uploads" + File.separator + "New file.pdf")
        if (mFile.createNewFile()) {
            println("File created: " + mFile.name)
        } else {
            println("File already exists.")
        }

        val path = Paths.get(System.getProperty("user.dir") + "/Uploads" + File.separator + "New file.pdf")

        try {
            Files.newOutputStream(path).use { os ->
                os.write(file.bytes)
            }
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
        return File(path.toString())
    }
}
