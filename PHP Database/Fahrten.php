<?php
/**
 * Created by PhpStorm.
 * User: BSD
 * Date: 06.07.2015
 * Time: 22:14
 */
/*$connection = mysql_connect("localhost","root","")
                or die ("keine Verbindung möglich. Benutzername oder Passwort sind falsch");
mysql_select_db("im_challenge")
                or die ("Die Datenbank existiert nicht.");

*/
$startTime = microtime(true);
printf("<<< Ready? Go! >>> <br><br>");
$mysqli = new mysqli("localhost", "root", "", "im_challenge");

/* check connection */
if ($mysqli->connect_errno) {
    printf("Connect failed: %s\n", $mysqli->connect_error."<br>");
    exit();
}

//$query = "SELECT fzgf.ABFAHRT, (fzgf.ABFAHRT + fzpe.ANKUNFT), (fzgf.ABFAHRT + fzpe.ABFAHRT), l.PUBDIVALINNAM, hs.NR, hs.NAME, hs.XKOORD, hs.YKOORD FROM fahrzeugfahrtabschnitt fzgfa ".
//            "LEFT JOIN fzgfahrt fzgf ON fzgfa.FZGFAHRTNR = fzgf.NR ".
//            "LEFT JOIN fahrzeitprofilelement fzpe ON fzgf.LINROUTENAME = fzpe.LINROUTENAME AND fzgf.FZPROFILNAME = fzpe.FZPROFILNAME ".
//            "LEFT JOIN linie l ON fzpe.LINNAME = l.NAME ".
//            "LEFT JOIN linienroutenelement lre ON fzpe.LRELEMINDEX = lre.INDEX ".
//            "LEFT JOIN haltepunkt hp ON lre.HPUNKTNR = hp.NR ".
//            "LEFT JOIN haltestellenbereich hsb ON hp.HSTBERNR = hsb.NR ".
//            "LEFT JOIN haltestellen hs ON hsb.HSTNR = hs.NR ".
//            "WHERE fzgfa.VTAGNR = 2 LIMIT 100000";
////            "ORDER BY fzpe.INDEX ASC ";
//    printf($query."<br>");
//
//if ($fzgfahrten = $mysqli->query($query)) {
//    printf("Profilelemente: ".$fzgfahrten->num_rows."<br>");
//    $fzgfahrten->free();
//}




printf("Allocating trips...<br>");

$query = "SELECT fzgf.ABFAHRT, fzgf.LINROUTENAME, fzgf.FZPROFILNAME FROM fahrzeugfahrtabschnitt fzgfa ".
            "LEFT JOIN fzgfahrt fzgf ON fzgfa.FZGFAHRTNR = fzgf.NR ".
            "WHERE fzgfa.VTAGNR = 2 ".
            "ORDER BY fzgfa.FZGFAHRTNR ASC";
$batch = array();

if ($fzgfahrten = $mysqli->query($query)) {
    printf("Fahrzeugfahrten: %s<br>", $fzgfahrten->num_rows);

    $first = true;
    $count = 0;
    $jquery = "INSERT INTO im1 VALUES ";
    $jcount = 0;
    while ($row = $fzgfahrten->fetch_assoc()) {

        $query = "SELECT fzpe.ANKUNFT, fzpe.ABFAHRT, l.PUBDIVALINNAM, hsb.NR HSBNR, hs.NR, hs.NAME, hs.XKOORD, hs.YKOORD FROM fahrzeitprofilelement fzpe ".
                    "LEFT JOIN linie l ON fzpe.LINNAME = l.NAME ".
                    "LEFT JOIN linienroutenelement lre ON fzpe.LRELEMINDEX = lre.INDEX AND fzpe.LINROUTENAME = lre.LINROUTENAME ".
                    "LEFT JOIN haltepunkt hp ON lre.HPUNKTNR = hp.NR ".
                    "LEFT JOIN haltestellenbereich hsb ON hp.HSTBERNR = hsb.NR ".
                    "LEFT JOIN haltestellen hs ON hsb.HSTNR = hs.NR ".
                    "WHERE fzpe.LINROUTENAME = '".$row["LINROUTENAME"]."' AND fzpe.FZPROFILNAME = ".$row["FZPROFILNAME"]." ".
                    "ORDER BY fzpe.INDEX ASC ";

        if ($fahrten = $mysqli->query($query)) {
            $count += $fahrten->num_rows;
            $count--;

            $row_1 = $fahrten->fetch_assoc();
            while ($row_0 = $fahrten->fetch_assoc()) {
//                $entry["Ab_HST_Nr"] = $row_1["NR"];
//                $entry["Ab_HST_Name"] = '"'.$row_1["NAME"].'"';
//                $entry["ABFAHRT"] = '"'.calc_date($row["ABFAHRT"], $row_1["ABFAHRT"]).'"';
//                $entry["LINIE"] = $row_1["PUBDIVALINNAM"];
//                $entry["An_HST_Nr"] = $row_0["NR"];
//                $entry["An_HST_Name"] = '"'.$row_0["NAME"].'"';
//                $entry["ANKUNFT"] = '"'.calc_date($row["ABFAHRT"], $row_0["ANKUNFT"]).'"';
//                $entry["DAUER"] = calc_length($row_1["ABFAHRT"], $row_0["ANKUNFT"]);
//                $entry["Ab_X"] = '"'.$row_1["XKOORD"].'"';
//                $entry["Ab_Y"] = '"'.$row_1["YKOORD"].'"';
//                $entry["An_X"] = '"'.$row_0["XKOORD"].'"';
//                $entry["An_Y"] = '"'.$row_0["YKOORD"].'"';

                $entry["Ab_HSTB_Nr"] = $row_1["HSBNR"];
                $entry["Ab_HST_Nr"] = $row_1["NR"];
                $entry["Ab_HST_Name"] = $row_1["NAME"];
                $entry["ABFAHRT"] = calc_date($row["ABFAHRT"], $row_1["ABFAHRT"]);
                $entry["LINIE"] = $row_1["PUBDIVALINNAM"];
                $entry["An_HSTB_Nr"] = $row_0["HSBNR"];
                $entry["An_HST_Nr"] = $row_0["NR"];
                $entry["An_HST_Name"] = $row_0["NAME"];
                $entry["ANKUNFT"] = calc_date($row["ABFAHRT"], $row_0["ANKUNFT"]);
                $entry["DAUER"] = calc_length($row_1["ABFAHRT"], $row_0["ANKUNFT"]);
                $entry["Ab_X"] = $row_1["XKOORD"];
                $entry["Ab_Y"] = $row_1["YKOORD"];
                $entry["An_X"] = $row_0["XKOORD"];
                $entry["An_Y"] = $row_0["YKOORD"];

                array_push($batch, $entry);
                $row_1 = $row_0;
            }

            /* free result set */
            $fahrten->free();
        }
    }

    /* free result set */
    $fzgfahrten->free();

    $count = count($batch);
    printf("Allocated " . $count . " rows<br>");

//    for ($i = 0; $i < ceil($count / 5000); $i++) {
//        $start = $i * 5000;
//        $end = $start + 4999;
//        if ($end > $count-1) {
//            $end = $count-1;
//        }
//
//        printf("Inserting batch from " . $start . " to " . $end . "<br>");
//        $jquery = "INSERT INTO im1 (Ab_HST_Nr, Ab_HST_Name, ABFAHRT, LINIE, An_HST_Nr, An_HST_Name, ANKUNFT, DAUER, Ab_X, Ab_Y, An_X, An_Y) VALUES (";
//        $jquery .= "(" . implode(", ", $batch[$start]) . ")";
//        printf("Sample row: " . implode(", ", $batch[$start]) . "<br>");
//        for ($j = $start+1; $j < $end; $j++) {
//            $jquery .= ", (" . implode(",", $batch[$j]) . ")";
//        }
//        $jquery .= ")";
//    }

    printf("Writing IM_Trips.csv...<br>");
    $file = fopen("C://xampp/htdocs/IM_Trips.csv","w");
    fputcsv($file, array("VHSBNR", "VNR", "VNAME", "ABFAHRT", "LINIE", "NHSBNR", "NNR", "NNAME", "ANKUNFT", "DAUER", "VX", "VY", "NX", "NY"), ";",'"');

    foreach ($batch as $line)
    {
        fputcsv($file,$line,";",'"');
    }

    fclose($file);

}



printf ("<<< Finished in  " . number_format(( microtime(true) - $startTime), 4) . " Seconds / " . number_format(( microtime(true) - $startTime) / 60, 2) . " Minutes >>> <br><br><br>");
$startTime = microtime(true);
printf("Allocating transfer times...<br>");

$query = "SELECT vhsb.NR VHSBNR, vhs.NR VNR, vhs.NAME VNAME, nhsb.NR NHSBNR, nhs.NR NNR, nhs.NAME NName, ugz.ZEIT ZEIT FROM uebergangsgehzeiten ugz ".
    "LEFT JOIN haltestellenbereich vhsb ON ugz.VONHSTBERNR = vhsb.NR ".
    "LEFT JOIN haltestellenbereich nhsb ON ugz.NACHHSTBERNR = nhsb.NR ".
    "LEFT JOIN haltestellen vhs ON vhsb.HSTNR = vhs.NR ".
    "LEFT JOIN haltestellen nhs ON nhsb.HSTNR = nhs.NR ".
    "ORDER BY ugz.VONHSTBERNR ASC";
$batch = array();

if ($uebergaenge = $mysqli->query($query)) {
    printf("Übergänge: %s<br>", $uebergaenge->num_rows);

    printf("Writing IM_Transfers.csv...<br>");
    $file = fopen("C://xampp/htdocs/IM_Transfers.csv","w");
    fputcsv($file, array("VHSBNR", "VNR", "VNAME", "NHSBNR", "NNR", "NNAME", "ZEIT"), ";",'"');

    while ($row = $uebergaenge->fetch_assoc()) {
        $row["ZEIT"] = rtrim($row["ZEIT"], "s");
        fputcsv($file,$row,";",'"');
    }

    fclose($file);
    printf ("<<< Finished in  " . number_format(( microtime(true) - $startTime), 4) . " Seconds / " . number_format(( microtime(true) - $startTime) / 60, 2) . " Minutes >>> <br><br><br>");
}

/* close connection */
$mysqli->close();

function calc_date($from, $add) {
    $from_array = explode(":", $from);
    $add_array = explode(":", $add);
//    printf("Add: ".$add.", From: ".$from." | ");
    $seconds = ($from_array[0] * 60 * 60);
    $seconds += ($from_array[1] * 60);
    $seconds += ($from_array[2]);
    $seconds += ($add_array[0] * 60 * 60);
    $seconds += ($add_array[1] * 60);
    $seconds += ($add_array[2]);
    return $seconds;
}

function calc_length($from, $to) {
    $from_array = explode(":", $from);
    $to_array = explode(":", $to);
//    printf("To: ".$to.", From: ".$from."<br>");
    $length = ($to_array[0] * 60 * 60) - ($from_array[0] * 60 * 60);
    $length += ($to_array[1] * 60) - ($from_array[1] * 60);
    $length += $to_array[2] - $from_array[2];
    return $length;
}