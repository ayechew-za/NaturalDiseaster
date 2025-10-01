package com.example.disease

import com.example.disease.data.AnnouncementResponse
import com.google.gson.Gson

object AnnouncementJsonReader {

    private val gson = Gson()

    // JSON data as constant
    private const val JSON_STRING = """
    {
        "status": "Success",
        "message": "Announcements List",
        "data": {
            "id": "01jy5y2eg40qnzfyrpdxgmt78w",
            "text": "စက်တင်ဘာလ ၂၈ ရက် ည ၁၀ နာရီ ၂၀ မိနစ်မှာ တိုင်ဖွန်းမုန်တိုင်းဗဟို ဗီယက်နမ်ကမ်းခြေကို ဖြတ်ကျော်ဝင်ရောက်ပြီးတဲ့နောက် အောက်တိုဘာ ၁ ရက်နေ့ နံနက် ၂ နာရီ ၃၀ မိနစ်အထိ မြန်မာပြည်မှာ နေရာအနှံ့အပြားမိုးရွာနိုင်၊ နေရာကွက်ပြီး မိုးများနိုင်ပါမယ်။🔸၂၀၂၄ ခုနှစ် တိုင်ဖွန်းမုန်တိုင်းယာဂိမုန်တိုင်းအကြွင်းအကျန်များလောက် မိုးများနိုင်ခြင်းမဖြစ်၊ ရေကြီးရေလျှံခြင်းဆိုးရွားစွာဖြစ်ခြင်း မရှိနိုင်ပါ။",
            "type": "be careful",
            "style": {
                "bgColor": "#FFFCF4",
                "textColor": "#EEA205",
                "borderColor": "#EEA205"
            },
            "created_at": "2025-06-20T05:48:02.000000Z",
            "updated_at": "2025-09-28T19:37:39.000000Z"
        }
    }
    """

    // Main method to read and parse JSON
    fun readAnnouncement(): AnnouncementResponse {
        return gson.fromJson(JSON_STRING, AnnouncementResponse::class.java)
    }

    // Method to print announcement details
    fun printAnnouncementDetails() {
        val announcement = readAnnouncement()

        println("Status: ${announcement.status}") // Success
        println("Message: ${announcement.message}") // Announcements List
        println("Announcement Text: ${announcement.data?.text}")
        println("Type: ${announcement.data?.type}") // be careful
        println("BG Color: ${announcement.data?.style?.bgColor}") // #FFFCF4
        println("Text Color: ${announcement.data?.style?.textColor}") // #EEA205
        println("Border Color: ${announcement.data?.style?.borderColor}") // #EEA205
        println("Created At: ${announcement.data?.createdAt}")
        println("Updated At: ${announcement.data?.updatedAt}")
    }

    // Method to read from custom JSON string
    fun readFromCustomJson(customJson: String): AnnouncementResponse {
        return gson.fromJson(customJson, AnnouncementResponse::class.java)
    }
}