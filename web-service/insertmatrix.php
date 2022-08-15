<?php
define('HOST','34.170.20.242');
define('USER','root');
define('PASS','noflyzones22');
define('DB','nsf');

$con = mysqli_connect(HOST,USER,PASS,DB);

mysqli_query($con,'DROP TABLE IF EXISTS matrix');
mysqli_query($con,"CREATE TABLE matrix (columnID INTEGER, rowID INTEGER, val INTEGER, PRIMARY KEY (columnID,rowID))");

$entries = json_decode($_POST["data"]);

foreach ($entries as $entry){
    $val = $entry->Value;
    $row = $entry->RowID;
    $column = $entry->ColumnID;

    mysqli_query($con,"INSERT into matrix(rowID,columnID,val) VALUES ('$row','$column','$val')");
}

mysqli_close($con);
?>