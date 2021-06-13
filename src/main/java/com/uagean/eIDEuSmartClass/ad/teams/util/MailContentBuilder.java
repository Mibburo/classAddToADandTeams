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
            "<ol>\n" +
            "  <li>\n" +
            "    Authenticate with your Microsoft account which corresponds to the email address you have registered with the Course, a few minutes ago (<a href=\"https://login.microsoftonline.com/common/oauth2/v2.0/authorize?response_type=id_token&scope=openid profile&client_id=5e3ce6c0-2b1f-4285-8d4b-75ee78787346&redirect_uri=https://teams.microsoft.com/go&state=eyJpZCI6IjM3M2FjMGFhLTQ4ODctNDZlNi1iNmI2LWU1OTFjODgyYmZiYiIsInRzIjoxNjIzNDI4Mzg4LCJtZXRob2QiOiJyZWRpcmVjdEludGVyYWN0aW9uIn0=&nonce=878a44c2-675d-41e3-8a6d-4ab2fd3122cd&client_info=1&x-client-SKU=MSAL.JS&x-client-Ver=1.3.4&client-request-id=6ed2c6cf-b260-4e72-989b-df67dedffea8&response_mode=fragment\">Sign in</a>).\n" +
            "    If you haven’t  an account with Microsoft, you can open one by following the link: <a href=\"https://signup.microsoft.com/create-account/signup?products=CFQ7TTC0K8P5:0004&culture=en-us&country=WW&lm=deeplink&lmsrc=homePageWeb&cmpid=FreemiumSignUpHero&ali=1\">New to Teams: Sign up now</a>\n" +
            "  </li>\n" +
            "  <li>\n" +
            "    Download the desktop and mobile app (for better use of the provided functionality) -\n" +
            "    <a href=\"https://www.microsoft.com/en-ww/microsoft-teams/download-app \">https://www.microsoft.com/en-ww/microsoft-teams/download-app</a>\n" +
            "  </li>\n" +
            "</ol>\n" +
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


    public static String buildEmail(){
        return String.format(header + body + footer);
    }

}
