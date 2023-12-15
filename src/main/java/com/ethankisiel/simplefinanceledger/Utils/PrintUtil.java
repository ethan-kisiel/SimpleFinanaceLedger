package com.ethankisiel.simplefinanceledger.Utils;

import javafx.print.PageLayout;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PrintUtil
{
    public static void printView(Node view)
    {
        Printer printer = Printer
                .getAllPrinters()
                .stream()
                .filter(p -> p.getName().contains("PDF"))
                .findFirst()
                .orElse(Printer.getDefaultPrinter());



        // Create a printer job and get the default page layout
        PrinterJob job = PrinterJob.createPrinterJob(printer);
        PageLayout pageLayout = job.getJobSettings().getPageLayout();
        // JavaFX provides a "cross-feed" resolution as well, but we assume the printer has the same resolution
        // along both the X and the Y axis.
        double printDPI = job.getJobSettings().getPrintResolution().getFeedResolution();
        // One JavaFX coordinate unit will equal one point (1/72 of an inch) on the print. This scale factor will
        // therefore convert from points to actual pixels at the printer's resolution.
        double pt2px = printDPI / 72.0;
        // Compute the extents, in pixels, of the printable area of the page
        int width = (int) (pageLayout.getPrintableWidth() * pt2px);
        int height = (int) (pageLayout.getPrintableHeight() * pt2px);


        // Finally, we can now print the ImageView
        //job.printPage()
        // We can now submit additional pages, or we can end the job here so everything gets sent to the printer
        job.endJob();

    }

}
