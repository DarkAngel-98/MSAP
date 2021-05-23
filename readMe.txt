Фаза 1:
1. При реализирање на првата фаза од проектот по предметот Мобилни сервиси со андроид програмирање, искористен е кодот: neverEndingProcessAndroid7 --master 
(тоа е вториот линк од word документот).

2. За цел на отстранување на GUI-то искористени се следните чекори:

	2.1. Во android manifest, поставувам android:theme="@android:style/Theme.NoDisplay 
	2.2. Во MainActivity наместо extends AppCompatActivity, имам само public class MainActivity extends Activity
	2.3. Во MainActivity, во onCreate методот ја коментирам линијата за setContentView
	2.4. Во MainActivity повикувам finish() во onResume.

3. Во класата MainActivity имаме 2 метода - onCreate и onResume. 
Разликата ако finish() го сместиме во onCreate методот е во тоа што откако ќе се стартува апликацијата, се извршуваат фазите onCreate и onDestroy, односно не се 
извршува onResume методот.
Доколку пак, finish() го ставиме на крај од onResume методот, ќе поминат сите фазите: onCreate и onResume (ако додадеме уште методи, мислам дека и тие ќе поминат ?).

4. За класата ProcessMainClass идејата е да креираме static intent кој ќе ја стартува класата Service. При креирање на intent-от, како прв аргумент го 
ставаме Context context,
а како втор аргумент ја ставаме Service класата со Service.class -> serviceIntent = new Intent(context, Service.class) ; Потоа ја проверуваме андроид верзијата зошто
ако version<O имаме едноставен сервис кој го стартуваме со context.startService(serviceIntent), а во спротивно, ако Build.VERSION.SDK_INT >= Build.VERSION_CODES.O, 
тогаш имаме foreground service и го стартуваме со context.startForegroundService(serviceIntent).

5. Во класата Service ги сместуваме методите кои се грижат за рестартирање на foreground (тоа беше во случај ако Build.VERSION.SDK_INT >= Build.VERSION_CODES.O), 
ако сервисот бил kill-нат од страна на андроид, мора да го реиницијализираме и тоа го правиме со методот onStartCommand. Исто така во оваа класа е сместен
restartForeground методот кој се извршува ако имаме верзија M (Marshmallow). Имаме метод onDestroy во кој правиме broadcast на интент со наредбата:
sendBroadcast(broadcastIntent); со која се праќа интентот до сите BroadcastReceivers што би имале корист од овој интент.



Фаза 2:

6. При реализирање на фаза 2 додадени се 2 класи. Во класата CheckingNetworkConnection креирам HTTP конекција и го преземам тоа што се наоѓа на соодветна адреса. 
Во класата PingTask (која прави extends AsyncTask<Void, Void, String> ) го земам добиениот податок во doInBackground (за да не ја оптоваруваме главната нитка), 
а во методот onPostExecute ги печатам во лог добиените резултати и ја креирам ping командата.
во предефинираниот метод doInBackground. 


8. Во класата Service проверувам конективност со ConnectivityManager, го креирам тајмерот за PING командата. PingTask().execute() го повикувам кај методите 
restartForeground и onDestroy. 


Фаза 3:

9. Додадена е нова класа HttpPost во која ги преземам одговорите од POST наредбата на соодветна адреса. За таа работа, го користам ping од класата PingTask. 

10. Исто така, во класата Service додадено е зачувување на три последователни ping резултати во случај кога не сме конектирани на интернет. Резултатите се зачувуваат
со користење на sharedPreferences во функцијата saveData() (во класата Service). Исто така, додадена е и функција loadData() (која ги вади зачуваните ping стрингови) 
од тип String и идејата е тие стрингови да ги искористам ако сакам да пратам ping во класата со POST методот, меѓутоа тој дел не е реализиран.   