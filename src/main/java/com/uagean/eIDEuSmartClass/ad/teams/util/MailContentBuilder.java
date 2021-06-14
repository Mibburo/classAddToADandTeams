/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uagean.eIDEuSmartClass.ad.teams.util;

public class MailContentBuilder {

    private final static String header = "<!DOCTYPE html>\n"
            + "<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\" >\n"
            + "    <head>\n"
            + "        <title>test title</title>\n"
            + "    </head>\n";

    private final static String body = "<body>\n" +
            "Welcome!\n" +
            "<p>\n" +
            "  We have invited you to access the Teams which support the Online Interactive Learning Environment of the Course.\n" +
            "</p>\n" +
            "<p>\n" +
            "An email invitation sent to your email account from Microsoft (on behalf of i4mLab@UAegean)<br>\n" +
            "  “Microsoft Invitations on behalf of i4mLab@UAegean <<a href = \"mailto: invites@microsoft.com\">invites@microsoft.com</a>>”<br>\n" +
            "Please Accept by clicking on the link “Accept invitation”, then follow the instructions<br>\n" +
            "</p>\n" +
            "\n" +
            "<p>\n" +
            "If you accept the invitation sent by Microsoft (on behalf of i4mLab@UAegean), you will be redirected to the Teams landing page, where you can:\n" +
            "</p>\n" +
            "<p>\n" +
            "<ul>\n" +
            "  <li>\n" +
            "    Authenticate with Microsoft, In the Sign in form, enter the email address you have registered with the UAegean-HP SMart Class - Email: < %1$s > \n" +
            "  </li>\n" +
            "  <li>\n" +
            "   If it not accepted, you have to open a new account with Microsoft where the username should be the email address you  have registered with the UAegean-HP SMart Class - Email: < %1$s >" +
            "  </li>\n" +
            "  <li>\n" +
            "    Download the desktop and mobile app (for better use of the provided functionality) -\n" +
            "    <a href=\"https://www.microsoft.com/en-ww/microsoft-teams/download-app \">https://www.microsoft.com/en-ww/microsoft-teams/download-app</a>\n" +
            "  </li>\n" +
            "</ul>\n" +
            "    </p>\n" +
            "\n" +
            "<p>\n" +
            "After successful authentication, you may proceed with Teams from the desktop/mobile app -- or from the web interface of Teams, if you haven’t downloaded the app.\n" +
            "</p>\n" +
            "<p>\n" +
            "<b>Important</b>: When you enter Teams, please select the domain “i4mLab@UAegean (Guest)'' at the right corner of the application.\n" +
            "</p>\n" +
            "<p>\n" +
            "You are IN!: Select your <b>Course</b> Team\n" +
            "<br>\n" +
            "Introduction to e-Privacy and Cybersecurity: Technology and Policy issues \n" +
            "</p>\n";

    private final static String footer
           = "<p>\n" +
            "<b>Organization:  i4mLab@UAegean (UAegean | i4m Lab)<br>\n" +
            "  More information on how to use Microsoft Teams can be found below:</b><br>\n" +
            "  <a href=\"https://i4mlabuaegean.sharepoint.com/:w:/s/i4mlabprojects/EbKBPQD6tx1LnLmuPQbKt7cB-ZkJAaXsE2QZOGe1nzbQkQ?e=A4sSfJ\">MS Word document</a><br>\n" +
            "  <a href=\"https://docs.google.com/document/d/1mJRV0v06pa6tnrYPSnOU2vFV7t8sDPlE_Q_2Zlc1LWI/edit?usp=sharing\">Google Drive document</a>\n" +
            "</p>\n" +
            "</body>\n"
            + "</html>";


    public static String buildEmail(String email){
        return String.format(header + body + footer, email);
    }

}
