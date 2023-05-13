package com.acetechsol.auththree

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.acetechsol.auththree.adapters.AttendanceReportPreviewAdapter
import com.acetechsol.auththree.database.Constants
import com.acetechsol.auththree.database.DbHelper
import com.acetechsol.auththree.models.ModelCountStaffAttendance
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.itextpdf.io.image.ImageData
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.io.source.ByteArrayOutputStream
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.property.TextAlignment
import com.itextpdf.layout.property.UnitValue
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import android.graphics.drawable.BitmapDrawable
import com.acetechsol.auththree.models.ModelStaffAttendance
import com.itextpdf.layout.borders.Border
import com.itextpdf.layout.property.HorizontalAlignment


class AttendanceReportPreviewActivity : AppCompatActivity() {

    private lateinit var dbHelper: DbHelper
    private lateinit var reportPreviewRV: RecyclerView
    private lateinit var recordList: ArrayList<ModelStaffAttendance>
    private lateinit var presentAbsentRecordList: ArrayList<ModelStaffAttendance>
    private lateinit var countStaffAttendance: ArrayList<ModelCountStaffAttendance>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attendance_report_preview)

        dbHelper = DbHelper(this)
        reportPreviewRV = findViewById(R.id.report_preview_RV)

        val queryFromDate = intent.getStringExtra("FROM_QUERY_DATE")
        val queryToDate = intent.getStringExtra("TO_QUERY_DATE")
        var orderBy = intent.getStringExtra("ORDER_BY")

        orderBy = if(orderBy == "Dates"){
            Constants.ATTENDANCE_ID
        }else{
            "STAFF_NAME"
        }



        //Toast.makeText(this, queryDate, Toast.LENGTH_SHORT).show()

        supportActionBar?.title = "Report"

        loadReports(queryFromDate.toString(), queryToDate.toString(), orderBy.toString())

        val generatePdfFABtn: FloatingActionButton = findViewById(R.id.generate_Pdf_btn)

        generatePdfFABtn.setOnClickListener {
            generatePdf(queryFromDate, queryToDate, orderBy)
        }

    }

    private fun loadReports(queryFromDate: String, queryToDate: String, orderBy: String) {
        recordList = dbHelper.getDailyAttendanceRecords(queryFromDate, queryToDate, orderBy)
        val attendanceReportPreviewAdapter = AttendanceReportPreviewAdapter(this, recordList)
        reportPreviewRV.adapter = attendanceReportPreviewAdapter
    }


    @SuppressLint("SimpleDateFormat")
    private fun generatePdf(queryFromDate: String?, queryToDate: String?, orderBy: String) {
        val filePath = filesDir
        val file = File(filePath, "Attendance Report.pdf")

        val pdfWriter = PdfWriter(file)

        val pdfDocument = com.itextpdf.kernel.pdf.PdfDocument(pdfWriter)
        val document = com.itextpdf.layout.Document(pdfDocument, PageSize.A4)

        //list present from selected date

        val documentLogo = AppCompatResources.getDrawable(this, R.drawable.luxe_logo)
        val bitmap = (documentLogo as BitmapDrawable).bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val imageInByte = stream.toByteArray()

        val imageData: ImageData = ImageDataFactory.create(imageInByte)
        val image = Image(imageData)
        image.setHeight(50f)
        image.setMarginTop(16f)
        image.setHorizontalAlignment(HorizontalAlignment.RIGHT)


        val tableHeading = Table(
            UnitValue.createPercentArray(
                floatArrayOf(80f, 140f)
            )
        ).useAllAvailableWidth()


        tableHeading.addCell(Cell().add(image).setHorizontalAlignment(HorizontalAlignment.RIGHT).setBorder(
            Border.NO_BORDER))
        tableHeading.addCell(Cell().add(Paragraph("The Luxe Night Club \n A&C Mall")
            .setFontSize(20.0f)
            .setBold()
            .setTextAlignment(TextAlignment.LEFT)
            .setMargin(10f))
            .setBorder(Border.NO_BORDER))

        document.add(tableHeading)


        val departments = arrayOf("Management","Security", "Cashier", "Bar", "Kitchen", "Shisha", "Floor", "Housekeeping", "DJ")

        var textWithSpace = Paragraph("STAFF ATTENDANCE")
        textWithSpace.setMargins(2f, 10f, 10f,10f)
            .setTextAlignment(TextAlignment.CENTER)
            .setBold()
            .setUnderline()
        document.add(textWithSpace)

        //start present table grouped by department
        for(department in departments) {
            //fetch database
            presentAbsentRecordList = dbHelper.getPresentAttendanceRecords(
                queryFromDate.toString(),
                queryToDate.toString(),
                orderBy,
                department
            )

            if(presentAbsentRecordList.size <= 0){
                continue
            }


            textWithSpace = Paragraph(department)
                .setTextAlignment(TextAlignment.CENTER)
                .setMargins(2f, 10f, 10f, 10f)
                .setBold()
                .setUnderline()
                .setFontSize(14f)
                .setMarginTop(20f)
            document.add(textWithSpace)

            val fd = queryFromDate?.substring(8, 10) + "-" + queryFromDate?.substring(5, 7) + "-" + queryFromDate?.substring(0,4)
            val td = queryToDate?.substring(8, 10) + "-" + queryToDate?.substring(5, 7) + "-" + queryToDate?.substring(0,4)

            textWithSpace = if(queryFromDate == queryToDate) {
                Paragraph(fd)
            }else{
                Paragraph("$fd to $td")
            }
                .setTextAlignment(TextAlignment.CENTER)
                .setMargins(0f, 10f, 10f, 10f)
            document.add(textWithSpace)

            val tablePresent = Table(
                UnitValue.createPercentArray(
                    floatArrayOf(
                        4f,
                        20f,
                        12f,
                        10f,
                        10f,
                        10f,
                        16f,
                        10f,
                        10f
                    )
                )
            ).useAllAvailableWidth()

            //Table Header Cells
            tablePresent.addHeaderCell(
                Cell().add(
                    Paragraph("S/N").setTextAlignment(TextAlignment.CENTER).setBold()
                        .setFontSize(12f)
                )
            )
            tablePresent.addHeaderCell(
                Cell().add(
                    Paragraph("STAFF NAME").setTextAlignment(
                        TextAlignment.CENTER
                    ).setBold().setFontSize(12f)
                )
            )
            tablePresent.addHeaderCell(
                Cell().add(
                    Paragraph("DATE").setTextAlignment(TextAlignment.CENTER).setBold()
                        .setFontSize(12f)
                )
            )
            tablePresent.addHeaderCell(
                Cell().add(
                    Paragraph("SHIFT").setTextAlignment(TextAlignment.CENTER).setBold()
                        .setFontSize(12f)
                )
            )
            tablePresent.addHeaderCell(
                Cell().add(
                    Paragraph("TIME IN").setTextAlignment(
                        TextAlignment.CENTER
                    ).setBold().setFontSize(12f)
                )
            )
            tablePresent.addHeaderCell(
                Cell().add(
                    Paragraph("CASH IN").setTextAlignment(
                        TextAlignment.CENTER
                    ).setBold().setFontSize(12f)
                )
            )
            tablePresent.addHeaderCell(
                Cell().add(
                    Paragraph("PHONE DESC").setTextAlignment(
                        TextAlignment.CENTER
                    ).setBold().setFontSize(12f)
                )
            )
            tablePresent.addHeaderCell(
                Cell().add(
                    Paragraph("TIME OUT").setTextAlignment(
                        TextAlignment.CENTER
                    ).setBold().setFontSize(12f)
                )
            )
            tablePresent.addHeaderCell(
                Cell().add(
                    Paragraph("CASH OUT").setTextAlignment(
                        TextAlignment.CENTER
                    ).setBold().setFontSize(12f)
                )
            )
            //count for s/n
            var count = 1


            //populate records
            for (record in presentAbsentRecordList) {
                //modelStaffRecord = dbHelper.getStaffDetails(record.staffId)
                tablePresent.addCell(
                    Cell().add(
                        Paragraph(count.toString()).setTextAlignment(
                            TextAlignment.LEFT
                        ).setFontSize(11f)
                    )
                )
                tablePresent.addCell(record.staffName)
                    .setTextAlignment(TextAlignment.LEFT).setFontSize(11f)
                tablePresent.addCell(
                    Cell().add(
                        Paragraph(record.dateIn).setTextAlignment(
                            TextAlignment.LEFT
                        ).setFontSize(11f)
                    )
                )
                tablePresent.addCell(
                    Cell().add(
                        Paragraph(record.session.lowercase()).setTextAlignment(
                            TextAlignment.LEFT
                        ).setFontSize(11f)
                    )
                )
                tablePresent.addCell(
                    Cell().add(
                        Paragraph(record.timeIn).setTextAlignment(
                            TextAlignment.LEFT
                        ).setFontSize(11f)
                    )
                )
                tablePresent.addCell(
                    Cell().add(
                        Paragraph("GH₵ ${record.cashIn}").setTextAlignment(
                            TextAlignment.JUSTIFIED
                        ).setFontSize(11f)
                    )
                )
                tablePresent.addCell(
                    Cell().add(
                        Paragraph(record.phoneDesc).setTextAlignment(
                            TextAlignment.LEFT
                        )
                    )
                )
                tablePresent.addCell(
                    Cell().add(
                        Paragraph(record.timeOut).setTextAlignment(
                            TextAlignment.LEFT
                        )
                    )
                )
                tablePresent.addCell(
                    Cell().add(
                        Paragraph("GH₵ ${record.cashOut}").setTextAlignment(
                            TextAlignment.JUSTIFIED
                        ).setFontSize(11f)
                    )
                )
                count++
            }

            //add table to document
            document.add(tablePresent)
            //add break between tables
            document.add(Paragraph("\n"))
        }
        //end table




        var y = 0
        for(department in departments) {
            presentAbsentRecordList = dbHelper.getAbsentAttendanceRecords(
                queryFromDate.toString(),
                queryToDate.toString(),
                orderBy,
                department
            )

            if(presentAbsentRecordList.size <=0){
                continue
            }

            y++
            if(y == 1) {//list absent with permission
                textWithSpace = Paragraph("STAFF ABSENT WITH PERMISSION")
                textWithSpace.setMargins(20f, 10f, 10f, 10f)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBold()
                    .setUnderline()
                document.add(textWithSpace)
            }

            textWithSpace = Paragraph(department)
                .setTextAlignment(TextAlignment.CENTER)
                .setMargins(0f, 10f, 10f, 10f)
                .setBold()
                .setUnderline()
                .setFontSize(14f)
                .setMarginTop(20f)
            document.add(textWithSpace)

            textWithSpace = if(queryFromDate == queryToDate) {
                Paragraph("$queryFromDate")
            }else{
                Paragraph("$queryFromDate to $queryToDate")
            }
            textWithSpace.setMargins(0f, 10f, 10f,10f)
                .setTextAlignment(TextAlignment.CENTER)
            document.add(textWithSpace)

            val tableAbsent = Table(
                UnitValue.createPercentArray(
                    floatArrayOf(
                        4f,
                        28f,
                        12f,
                        10f,
                        26f
                    )
                )
            ).useAllAvailableWidth()

            tableAbsent.addHeaderCell(Cell().add(Paragraph("S/N").setTextAlignment(TextAlignment.CENTER)))
            tableAbsent.addHeaderCell(
                Cell().add(
                    Paragraph("STAFF NAME").setTextAlignment(
                        TextAlignment.CENTER
                    )
                )
            )
            tableAbsent.addHeaderCell(Cell().add(Paragraph("DATE").setTextAlignment(TextAlignment.CENTER)))
            tableAbsent.addHeaderCell(Cell().add(Paragraph("SHIFT").setTextAlignment(TextAlignment.CENTER)))
            tableAbsent.addHeaderCell(Cell().add(Paragraph("REASON").setTextAlignment(TextAlignment.CENTER)))


            var count = 1

            for (record in presentAbsentRecordList) {
                //val modelStaffRecord = dbHelper.getStaffDetails(record.staffId)
                tableAbsent.addCell(
                    Cell().add(
                        Paragraph(count.toString()).setTextAlignment(
                            TextAlignment.LEFT
                        ).setFontSize(12f)
                    )
                )
                tableAbsent.addCell(record.staffName)
                    .setTextAlignment(TextAlignment.LEFT).setFontSize(12f)

                tableAbsent.addCell(
                    Cell().add(
                        Paragraph(record.dateIn).setTextAlignment(
                            TextAlignment.LEFT
                        ).setFontSize(12f)
                    )
                )
                tableAbsent.addCell(
                    Cell().add(
                        Paragraph(record.session.lowercase()).setTextAlignment(
                            TextAlignment.LEFT
                        )
                    )
                )
                tableAbsent.addCell(
                    Cell().add(
                        Paragraph(record.permission).setTextAlignment(
                            TextAlignment.LEFT
                        )
                    )
                )

                count++
            }

            document.add(tableAbsent)
            document.add(Paragraph("\n"))
        }

        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val date1: Date = formatter.parse(queryFromDate.toString()) as Date
        val date2: Date = formatter.parse(queryToDate.toString()) as Date
        val cal1 = Calendar.getInstance()
        val cal2 = Calendar.getInstance()
        cal1.time = date1
        cal2.time = date2


        if(cal1 < cal2) {//list absent with permission
            textWithSpace =
                Paragraph("Total Attendance per staff from $queryFromDate to $queryToDate")
            textWithSpace.setMargins(30f, 10f, 10f, 10f)
                .setBold()
                .setUnderline()
                .setTextAlignment(TextAlignment.CENTER)
            document.add(textWithSpace)

            val tableCountStaffAttendance = Table(
                UnitValue.createPercentArray(
                    floatArrayOf(
                        4f,
                        20f,
                        10f,
                        10f
                    )
                )
            ).useAllAvailableWidth()

            tableCountStaffAttendance.addHeaderCell(
                Cell().add(
                    Paragraph("S/N").setTextAlignment(
                        TextAlignment.CENTER
                    ).setBold().setFontSize(12f)
                )
            )
            tableCountStaffAttendance.addHeaderCell(
                Cell().add(
                    Paragraph("STAFF NAME").setTextAlignment(
                        TextAlignment.CENTER
                    ).setBold().setFontSize(12f)
                )
            )
            tableCountStaffAttendance.addHeaderCell(
                Cell().add(
                    Paragraph("DEPT").setTextAlignment(
                        TextAlignment.CENTER
                    ).setBold().setFontSize(12f)
                )
            )
            tableCountStaffAttendance.addHeaderCell(
                Cell().add(
                    Paragraph("TOTAL ATTENDANCE").setTextAlignment(
                        TextAlignment.CENTER
                    ).setBold().setFontSize(12f)
                )
            )

            var count = 1

            countStaffAttendance =
                dbHelper.countStaffAttendance(queryFromDate.toString(), queryToDate.toString())

            for (i in countStaffAttendance) {
                val modelStaffRecord = dbHelper.getStaffDetails(i.staffId)
                tableCountStaffAttendance.addCell(
                    Cell().add(
                        Paragraph(count.toString()).setTextAlignment(
                            TextAlignment.LEFT
                        ).setFontSize(12f)
                    )
                )
                tableCountStaffAttendance.addCell(
                    Cell().add(
                        Paragraph("${modelStaffRecord.surname} ${modelStaffRecord.otherName}").setTextAlignment(
                            TextAlignment.LEFT
                        ).setFontSize(12f)
                    )
                )
                tableCountStaffAttendance.addCell(
                    Cell().add(
                        Paragraph(modelStaffRecord.department).setTextAlignment(
                            TextAlignment.LEFT
                        ).setFontSize(12f)
                    )
                )
                tableCountStaffAttendance.addCell(
                    Cell().add(
                        Paragraph(i.numberAttended).setTextAlignment(
                            TextAlignment.LEFT
                        ).setFontSize(12f)
                    )
                )
                count++
            }

            document.add(tableCountStaffAttendance)
        }else if(cal1 > cal2){
            Toast.makeText(this, "Please make sure your FROM DATE is older than your TO DATE", Toast.LENGTH_SHORT).show()
        }

        document.close()

        //Toast.makeText(this, "Report created...", Toast.LENGTH_LONG).show()

        //Timer().schedule(5000) {

            sharePdf(file)


        //}

    }

    private fun sharePdf(file: File) {
        val intentShare = Intent(Intent.ACTION_SEND)
        intentShare.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        intentShare.type = "application/pdf"
        val uri = FileProvider.getUriForFile(this, applicationContext.packageName + ".provider", file)
        //intentShare.setDataAndType(uri, "application/pdf");
        //startActivity(intentShare)


        intentShare.putExtra(Intent.EXTRA_STREAM, uri)
        intentShare.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivity(Intent.createChooser(intentShare, "Share the file ..."))
    }



}