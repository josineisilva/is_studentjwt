<?php
$url = 'http://localhost:8080/usuario/perfil/'.(string)$argv[1];
$data = array(
  "perfil_id" => (int)$argv[2],
);
$postdata = json_encode($data);
$authorization = "Authorization: Bearer ".str_replace("\n","",file_get_contents("jwt.txt"));
$curl = curl_init($url);
curl_setopt($curl, CURLOPT_POST, 1);
curl_setopt($curl, CURLOPT_CUSTOMREQUEST, "DELETE");
curl_setopt($curl, CURLOPT_POSTFIELDS, $postdata);
curl_setopt($curl, CURLOPT_RETURNTRANSFER, 1);
curl_setopt($curl, CURLOPT_HTTPHEADER, array('Content-Type: application/json' , $authorization ));
$result = curl_exec($curl);
$httpcode = curl_getinfo($curl, CURLINFO_HTTP_CODE);
curl_close($curl);
echo 'HTTP code: ' . $httpcode;
echo "\n";
$json =json_decode($result);
is_null($json) ? print_r($result) : print_r(json_encode($json,JSON_PRETTY_PRINT));
echo "\n";
?>
