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






$query = "SELECT fzgf.ABFAHRT, fzgf.LINROUTENAME, fzgf.FZPROFILNAME FROM fahrzeugfahrtabschnitt fzgfa ".
            "LEFT JOIN fzgfahrt fzgf ON fzgfa.FZGFAHRTNR = fzgf.NR ".
            "WHERE fzgfa.VTAGNR = 2 ".
            "ORDER BY fzgfa.FZGFAHRTNR ASC";

if ($fzgfahrten = $mysqli->query($query)) {
    printf("Fahrzeugfahrten: %s<br>", $fzgfahrten->num_rows);

    $first = true;
    $count = 0;
    $j = 0;
    $jquery = "INSERT INTO im1 VALUES ";
    $jcount = 0;
    while ($row = $fzgfahrten->fetch_assoc()) {
        $j++;
//        if ($first) {
//            printf("Erste Fahrt: Abfahrt %s, Linie %s, FZProfil %s <br>", $row["ABFAHRT"], $row["LINROUTENAME"], $row["FZPROFILNAME"]);
//        }

        $query = "SELECT fzpe.ANKUNFT, fzpe.ABFAHRT, l.PUBDIVALINNAM, hs.NR, hs.NAME, hs.XKOORD, hs.YKOORD FROM fahrzeitprofilelement fzpe ".
                    "LEFT JOIN linie l ON fzpe.LINNAME = l.NAME ".
                    "LEFT JOIN linienroutenelement lre ON fzpe.LRELEMINDEX = lre.INDEX AND fzpe.LINROUTENAME = lre.LINROUTENAME ".
                    "LEFT JOIN haltepunkt hp ON lre.HPUNKTNR = hp.NR ".
                    "LEFT JOIN haltestellenbereich hsb ON hp.HSTBERNR = hsb.NR ".
                    "LEFT JOIN haltestellen hs ON hsb.HSTNR = hs.NR ".
                    "WHERE fzpe.LINROUTENAME = '".$row["LINROUTENAME"]."' AND fzpe.FZPROFILNAME = ".$row["FZPROFILNAME"]." ".
                    "ORDER BY fzpe.INDEX ASC ";
//        printf("%s<br>", $query);

        $batch = array();
        if ($fahrten = $mysqli->query($query)) {
            $count += $fahrten->num_rows;
            $count--;

            $row_1 = $fahrten->fetch_assoc();
            while ($row_0 = $fahrten->fetch_assoc()) {
                $entry["Ab_HST_Nr"] = $row_1["NR"];
                $entry["Ab_HST_Name"] = '"'.$row_1["NAME"].'"';
                $entry["ABFAHRT"] = '"'.calc_date($row["ABFAHRT"], $row_1["ABFAHRT"]).'"';
                $entry["LINIE"] = $row_1["PUBDIVALINNAM"];
                $entry["An_HST_Nr"] = $row_0["NR"];
                $entry["An_HST_Name"] = '"'.$row_0["NAME"].'"';
                $entry["ANKUNFT"] = '"'.calc_date($row["ABFAHRT"], $row_0["ANKUNFT"]).'"';
                $entry["DAUER"] = calc_length($row_1["ABFAHRT"], $row_0["ANKUNFT"]);
                $entry["Ab_X"] = '"'.$row_1["XKOORD"].'"';
                $entry["Ab_Y"] = '"'.$row_1["YKOORD"].'"';
                $entry["An_X"] = '"'.$row_0["XKOORD"].'"';
                $entry["An_Y"] = '"'.$row_0["YKOORD"].'"';

                array_push($batch, $entry);
                $row_1 = $row_0;

//                if ($first) {
//                    printf("Erster Eintrag: %s <br>", implode(", ", $entry));
//                    $first = false;
//                }
            }

            $starti = 0;
            if ($j == 1) {
                $jquery .= "(".implode(", ",$batch[0]).")";
                $starti = 1;
            }

            for ($i = $starti; $i < count($batch); $i++) {
                $jquery .= ",(".implode(",",$batch[$i]).")";
            }

            $jcount += count($batch);

            if ($j == 100) {
                printf("Insert batch with ".$jcount." entries<br>");
                $mysqli->query($jquery);
                $j = 0;
                $jquery = "INSERT INTO im1 (Ab_HST_Nr, Ab_HST_Name, ABFAHRT, LINIE, An_HST_Nr, An_HST_Name, ANKUNFT, DAUER, Ab_X, Ab_Y, An_X, An_Y) VALUES ";
                $jcount = 0;
            }

            /* free result set */
            $fahrten->free();
        }

    }
    printf("Insert batch with ".$jcount." entries<br>");
    $mysqli->query($jquery);

    /* free result set */
    printf("Profilelemente: %s<br>", $count);
    $fzgfahrten->free();
}

/* close connection */
$mysqli->close();
printf ("<br><<< Finished in  " . number_format(( microtime(true) - $startTime), 4) . " Seconds / " . number_format(( microtime(true) - $startTime) / 60, 2) . " Minutes >>> <br>");

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