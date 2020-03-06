package com.example.arduinosensors;
 
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
  
public class MainActivity extends Activity {

  TextView   txtStringLength , txtnames;
  Handler bluetoothIn;
    ImageView musicImage;
  final int handlerState = 0;        				 //used to identify handler message
  private BluetoothAdapter btAdapter = null;
  private BluetoothSocket btSocket = null;
  private StringBuilder recDataString = new StringBuilder();
    long lastUpdate,timeDiff,currtime ;
    private String lastString ;
    private String currentGesture;
    MediaPlayer player;
   ArrayList<Integer> songs = new ArrayList<Integer>();
    ArrayList<Integer> images = new ArrayList<Integer>();
    ArrayList<String> names = new ArrayList<String>();
  private ConnectedThread mConnectedThread;
    private int currentSong = 0;
    
  // SPP UUID service - this should work for most devices
  private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
  
  // String for MAC address
  private static String address ;

@Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Log.i("hello","111111");
    setContentView(R.layout.activity_main);
  

   // txtString = (TextView) findViewById(R.id.txtString);
    txtStringLength = (TextView) findViewById(R.id.testView1);
    txtnames = (TextView) findViewById(R.id.music_name);
   // txtString.setText("");
    musicImage = (ImageView) findViewById(R.id.music_image);

    songs.add(R.raw.can);
    songs.add(R.raw.shape);
    songs.add(R.raw.friends);
    songs.add(R.raw.sugar);
    songs.add(R.raw.lost);
    songs.add(R.raw.alliswell);
    songs.add(R.raw.magic);
    songs.add(R.raw.wallah);
    songs.add(R.raw.tarja);
    songs.add(R.raw.gameof);
    songs.add(R.raw.mor);

    images.add(R.drawable.can);
    images.add(R.drawable.shape);
    images.add(R.drawable.friends);
    images.add(R.drawable.sugar);
    images.add(R.drawable.lost);
    images.add(R.drawable.alliswell);
    images.add(R.drawable.magic);
    images.add(R.drawable.wallah);
    images.add(R.drawable.tarja);
    images.add(R.drawable.gameof);
    images.add(R.drawable.mor);

    names.add("Can Bonomo - Doktor");
    names.add("Ed Sheeran - Shape Of You");
    names.add("Friends");
    names.add("Maroon5 - Sugar");
    names.add("LP - Lost On You");
    names.add("All Is Well");
    names.add("Magic - Rude");
    names.add("Wallah");
    names.add("Tarja");
    names.add("Game Of Thrones");
    names.add("Mor Ve Ötesi - Güneşi Beklerken");



    bluetoothIn = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == handlerState) {//if message is what we want
            	String readMessage = (String) msg.obj;// msg.arg1 = bytes from connect thread
                recDataString.append(readMessage);//keep appending to string until ~
                int endOfLineIndex = recDataString.indexOf("xxx");
                Log.i("index",Integer.toString(endOfLineIndex));// determine the end-of-line
                currtime =  System.currentTimeMillis();
                if (readMessage!= null && lastString!=null && !lastString.equalsIgnoreCase(readMessage)){
                    lastUpdate = currtime;
                    Log.i("last Update change", Long.toString(lastUpdate));
                }
                Log.i("current", Long.toString(currtime));
                Log.i("last Update ", Long.toString(lastUpdate));
                timeDiff = lastUpdate - currtime;
                Log.i("timeDir", Long.toString(timeDiff));
                lastString = readMessage;
               if (timeDiff>1000 || endOfLineIndex >-1) {// make sure there data before ~
                   ArrayList<Data> dataList = new ArrayList<Data>();
                    String dataInPrint = recDataString.substring(0, endOfLineIndex);    // extract string
                   // txtString.setText("Data Received = " + dataInPrint);

                   StringTokenizer stringTokenizer = new StringTokenizer(dataInPrint, "A");
                    int a=0;

                   while (stringTokenizer.hasMoreElements()) {
                       String IMUdata = stringTokenizer.nextElement().toString();
                       a++;
                     //  Log.i("user",IMUdata);

                       StringTokenizer IMUvalues = new StringTokenizer(IMUdata, "/");
                       try {
                           float ax=Float.valueOf(IMUvalues.nextElement().toString());
                           float ay=Float.valueOf(IMUvalues.nextElement().toString());
                           float az=Float.valueOf(IMUvalues.nextElement().toString());

                           float gx=Float.valueOf(IMUvalues.nextElement().toString());
                           float gy=Float.valueOf(IMUvalues.nextElement().toString());
                           float gz=Float.valueOf(IMUvalues.nextElement().toString());

                           float mx=Float.valueOf(IMUvalues.nextElement().toString());
                           float my=Float.valueOf(IMUvalues.nextElement().toString());
                           float mz=Float.valueOf(IMUvalues.nextElement().toString());

                           float yaw=Float.valueOf(IMUvalues.nextElement().toString());
                           float pitch=Float.valueOf(IMUvalues.nextElement().toString());
                           float roll=Float.valueOf(IMUvalues.nextElement().toString());

                           String print = "ax= " + ax +" ay= " + ay +" az= " + az +" gx= " + gx +" gy= " + gy +" gz= " + gz +" mx= " + mx +" my= " + my +" mz= " +  mz + " yaw= " + yaw + " pitch= " + pitch + " roll= " +roll;
                           Log.i("print ", print);


                           Data data = new Data(ax,ay,az,gx,gy,gz,mx,my,mz,yaw,pitch,roll);
                           dataList.add(data);
                       }catch (Exception e){
                           e.getStackTrace();
                       }

                   }


                   Log.i("print ", "--------");
                   Log.i("print ", "--------");
                   Log.i("print ", "--------");

                /*   Log.i("a",Integer.toString(a));
                   Log.i("Lenght",Integer.toString(dataList.size()));

                   Log.i("Ax","started");
                   isGoneRight(getAx(dataList));
                   Log.i("yaw","started");
                   isGoneRight(getYaws(dataList));*/

                   /*at2(getAvgAx(dataList));
                   isGoneRight(getAvgAx(dataList));
                   lookminmax(getAvgAx(dataList));*/
                  // getAvgAx(dataList);

                   float axAt2 = at2(getAvgAx(dataList));
                   int axIsGone = isGoneRight(getAvgAx(dataList));
                   int axLook = lookminmax(getAvgAx(dataList));

                   boolean isup = isUp(getAvgAy(dataList));
                   boolean isdown = isDown(getAvgAy(dataList));
                   boolean isUpSec = isUpSecong(getAvgAy(dataList));
                   float maxAZ = getMax(getAvgAz(dataList));

                    Log.i("Max AZ",Float.toString(maxAZ));
                     if ( (axIsGone == 0 && axLook == 0 )||isRight(getAx(dataList))){
                            Log.i("Right", "control done");
                            txtStringLength.setText("Right");
                            currentSong = currentSong + 1;
                            if (currentSong == songs.size()) currentSong = 0;
                            stopPlayer();
                            startPlayer(songs.get(currentSong));
                            txtnames.setText(names.get(currentSong));
                            musicImage.setImageResource(images.get(currentSong));

                   }else
                   if ( axIsGone == 1 && axLook == 1 &&  axAt2 < -200){
                            Log.i("Left", "control done");
                            txtStringLength.setText("Left");
                            currentSong = currentSong - 1;
                            if (currentSong == -1) currentSong = songs.size() - 1;
                            stopPlayer();
                            startPlayer(songs.get(currentSong));
                            txtnames.setText(names.get(currentSong));
                            musicImage.setImageResource(images.get(currentSong));

                   }else if ( maxAZ >1900 ||isup || isUpSec ){
                     //  if (Math.abs(ayAt2) > Math.abs(axAt2) + 100)
                       txtStringLength.setText("Up");
                       Log.i("Up","control done");
                       if(player != null){
                           player.stop();
                       }
                       txtnames.setText(names.get(currentSong));
                       musicImage.setImageResource(images.get(currentSong));
                   } else if (  isdown ){
                       //   if (Math.abs(ayAt2) > Math.abs(axAt2) + 100)
                       Log.i("Down","control done");
                       txtStringLength.setText("Down");
                       if(player != null){
                           player.stop();
                       }
                       txtnames.setText(names.get(currentSong));
                       musicImage.setImageResource(images.get(currentSong));
                   }else{
                       Log.i("Try","again");
                       txtStringLength.setText("Try again");
                   }


                   recDataString.delete(0, recDataString.length()); 					//clear all string data
                   // strIncom =" ";
                    dataInPrint = " ";
                }
            }
        }
    };
      
    btAdapter = BluetoothAdapter.getDefaultAdapter();       // get Bluetooth adapter
    checkBTState();	

  }



  private void startPlayer(int ID){

      player = new MediaPlayer();
      player.setAudioStreamType(AudioManager.STREAM_MUSIC);

      Uri mediaPath = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" +ID);
      try {
          player.setDataSource(getApplicationContext(), mediaPath);
      } catch (IOException e) {
          e.printStackTrace();
      }
      try {
          player.prepare();
          player.start();
      } catch (IOException e) {
          e.printStackTrace();
      }
  }

  private void stopPlayer(){

      if (player != null) {
          player.stop();
          player.release();
          player = null;
      }
  }

  private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
      
      return  device.createRfcommSocketToServiceRecord(BTMODULEUUID);
      //creates secure outgoing connecetion with BT device using UUID
  }
    
  @Override
  public void onResume() {
    super.onResume();
    
    //Get MAC address from DeviceListActivity via intent
    Intent intent = getIntent();
    
    //Get the MAC address from the DeviceListActivty via EXTRA
    address = intent.getStringExtra(DeviceListActivity.EXTRA_DEVICE_ADDRESS);

    //create device and set the MAC address
    BluetoothDevice device = btAdapter.getRemoteDevice(address);
     
    try {
        btSocket = createBluetoothSocket(device);
    } catch (IOException e) {
    	Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_LONG).show();
    }  
    // Establish the Bluetooth socket connection.
    try 
    {
      btSocket.connect();
        Log.i("done","1");
    } catch (IOException e) {
         Log.i("catch","1");
      try 
      {
        btSocket.close();
      } catch (IOException e2) 
      {
          Log.i("catch","2");
    	//insert code to deal with this 
      }
    }

      if(null == device) Log.i("boş","bos");
      else Log.i("ok","ok");

    mConnectedThread = new ConnectedThread(btSocket);
    mConnectedThread.start();


    
    //I send a character when resuming.beginning transmission to check device is connected
    //If it is not an exception will be thrown in the write method and finish() will be called
    mConnectedThread.write("x");
  }
  
  @Override
  public void onPause() 
  {
    super.onPause();
      stopPlayer();
    try
    {
    //Don't leave Bluetooth sockets open when leaving activity
      btSocket.close();
    } catch (IOException e2) {
    	//insert code to deal with this 
    }
  }

 //Checks that the Android device Bluetooth is available and prompts to be turned on if off 
  private void checkBTState() {
 
    if(btAdapter==null) { 
    	Toast.makeText(getBaseContext(), "Device does not support bluetooth", Toast.LENGTH_LONG).show();
    } else {
      if (btAdapter.isEnabled()) {
      } else {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, 1);
      }
    }
  }
  
  //create new class for connect thread
  private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
      
        //creation of the connect thread
        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
            	//Create I/O streams for connection
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }
      
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }
        
      
        public void run() {
            byte[] buffer = new byte[256];
            int bytes; 
 
            // Keep looping to listen for received messages
            while (true) {
                try {
                    bytes = mmInStream.read(buffer);        	//read bytes from input buffer
                    String readMessage = new String(buffer, 0, bytes);
                    // Send the obtained bytes to the UI Activity via handler
                    bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget(); 
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i("rundaki","catch");
                    break;
                }
            }
        }
        //write method
        public void write(String input) {
            byte[] msgBuffer = input.getBytes();           //converts entered String into bytes
            try {
                mmOutStream.write(msgBuffer);                //write bytes over BT connection via outstream
            } catch (IOException e) {  
            	//if you cannot write, close the application
            	Toast.makeText(getBaseContext(), "Connection Failure", Toast.LENGTH_LONG).show();
            	finish();
            	
              }
        	}
    	}

    public ArrayList<Float> getAvgAx(ArrayList<Data> datas){
        ArrayList<Float> axs =  new ArrayList();
        if(datas.size()== 0) return null;
        axs.add(datas.get(0).getAx());
        int i =0;
        while (i<datas.size()-2){
            float avgVal = 0;
            avgVal = datas.get(i).getAx()+datas.get(i+1).getAx()+datas.get(i+2).getAx();
            avgVal = avgVal/3;
            axs.add(avgVal);
            i +=1;

           // Log.i("avgAx",Float.toString(avgVal));
        }
        float lastone= datas.get(datas.size()-1).getAx();
        axs.add(lastone);
        return axs;
    }

    public ArrayList<Float> getAvgAz(ArrayList<Data> datas){
        ArrayList<Float> azs =  new ArrayList();
        for (int i = 0 ; i<datas.size();i++)
        azs.add(datas.get(i).getAz());

        return azs;
    }

    public ArrayList<Float> getAvgPitch(ArrayList<Data> datas){
        ArrayList<Float> azs =  new ArrayList();
        azs.add(datas.get(0).getPitch());
        int i =0;
        while (i<datas.size()-2){
            float avgVal ;
            avgVal = datas.get(i).getPitch()+datas.get(i+1).getPitch()+datas.get(i+2).getPitch();
            avgVal = avgVal/3;
            azs.add(avgVal);
            i +=1;
        }
        float lastone= datas.get(datas.size()-1).getPitch();
        azs.add(lastone);
        return azs;
    }

    public ArrayList<Float> getAvgRoll(ArrayList<Data> datas){
        ArrayList<Float> azs =  new ArrayList();
        azs.add(datas.get(0).getRoll());
        int i =0;
        while (i<datas.size()-2){
            float avgVal ;
            avgVal = datas.get(i).getRoll()+datas.get(i+1).getRoll()+datas.get(i+2).getRoll();
            avgVal = avgVal/3;
            azs.add(avgVal);
            i +=1;
        }
        float lastone= datas.get(datas.size()-1).getRoll();
        azs.add(lastone);
        return azs;
    }



    public ArrayList<Float> getAvgAy(ArrayList<Data> datas){
        ArrayList<Float> ays =  new ArrayList();
        ays.add(datas.get(0).getAy());
        int i =0;
        while (i<datas.size()-2){
            float avgVal ;
            avgVal = datas.get(i).getAy()+datas.get(i+1).getAy()+datas.get(i+2).getAy();
            avgVal = avgVal/3;
            ays.add(avgVal);
            i +=1;
        }
        float lastone= datas.get(datas.size()-1).getAy();
        ays.add(lastone);
        return ays;
    }


    public ArrayList<Float> getAx(ArrayList<Data> datas){
            ArrayList<Float> axs =  new ArrayList();
           for(int i =0; i<datas.size();i++){
               axs.add(datas.get(i).getAx());
           }
           return axs;
        }

        public int isGoneRight(ArrayList<Float> axs){
            if(axs != null || axs.size()==0) return 2;
            float max = axs.get(0);
            float min = axs.get(0);

            for(int i =1; i<axs.size();i++){
                if(axs.get(i)> max)  max = axs.get(i);
                if(axs.get(i)< min)  min = axs.get(i);
            }

            Log.i("Ax",Float.toString(axs.get(0)));
            Log.i("max",Float.toString(max));

            float maxDiff = max - axs.get(0);
            float minDiff = axs.get(0) - min;

            if (maxDiff > minDiff){
            //    Log.i("Right","yes");
                return 0;
            }else{
            //    Log.i("Left","yes");
                return 1;
            }
        }


        public  boolean isIncreasing (ArrayList<Float> axs){

            float max = axs.get(0);
            int maxindex=0;

            for(int i =1; i<axs.size();i++){
                if(axs.get(i)> max){
                    max = axs.get(i);
                    maxindex = i;
                }
            }

            if(maxindex!=0) return true;
            return false;
        }


    public int lookminmax(ArrayList<Float> axs){
        if(axs != null || axs.size()==0) return 2;
        float max = axs.get(0);
        int maxindex=0;
        int minindex=0;
        float min = axs.get(0);

        for(int i =1; i<axs.size();i++){
            if(axs.get(i)> max){
                max = axs.get(i);
                maxindex = i;
            }
            if(axs.get(i)< min){
                min = axs.get(i);
                minindex = i;
            }
        }

        float diff = max - min;
        int direction = maxindex - minindex;

        if (diff > 30) {
            if (direction > 0) {
             //   Log.i("MMRight", "yes");
                return 0;
            } else {
             //   Log.i(",MMLeft", "yes");
                return 1;
            }
        }
        return 2;
        }

        public float at2(ArrayList<Float> datas){
            float result =0;
            float avg = 0;
            if(datas != null){
                avg =  datas.get(0);


            for(int i =1; i<datas.size();i++){
                result += (datas.get(i)- avg);
            }

        }
            Log.i("At2",Float.toString(result));
            return result;
        }



    public int lookfistlast(ArrayList<Float> axs){
        float first=0 , last=0 ;
        if (axs.size()>1){
             first = axs.get(0)+ axs.get(1);
            first = first/2;
        }

        if (axs.size()>3){
             last = axs.get(axs.size()-2)+ axs.get(axs.size()-1);
            last = last/2;
        }


        float diff = first- last;

        if (Math.abs(diff) > 50) {
           return 0;
        }else
        return 1;
    }


    public boolean isUp(ArrayList<Float> axs){

        float max = axs.get(0);
        int maxindex=0;
        int minindex=0;
        float min = axs.get(0);

        for(int i =1; i<axs.size();i++){
            if(axs.get(i)> max){
                max = axs.get(i);
                maxindex = i;
            }
            if(axs.get(i)< min){
                min = axs.get(i);
                minindex = i;
            }
        }

        float diff = max - min;
        int direction = maxindex - minindex;

        if (diff > 30) {
            if (direction > 0 && maxindex != axs.size()-1) {
                //   Log.i("MMRight", "yes");

                return true;
            }else if(direction < 0 && maxindex != 0){
                return true;

            }
        }
       return false;

    }

    public  boolean isDown(ArrayList<Float> axs){
        float max = axs.get(0);
        int maxindex=0;
        int minindex=0;
        float min = axs.get(0);

        for(int i =1; i<axs.size();i++){
            if(axs.get(i)> max){
                max = axs.get(i);
                maxindex = i;
            }
            if(axs.get(i)< min){
                min = axs.get(i);
                minindex = i;
            }
        }

        float diff = max - min;
        int direction = maxindex - minindex;

        if (diff > 500 && direction>0) {
            return true;
        }
        return false;

    }

    public  boolean isUpSecong(ArrayList<Float> axs){
        float max = axs.get(0);
        float min = axs.get(0);

        for(int i =1; i<axs.size();i++){
            if(axs.get(i)> max){
                max = axs.get(i);
            }

            if(axs.get(i)< min){
                min = axs.get(i);
            }
        }

        Log.i("print avg",Float.toString(min));
        Log.i("print max",Float.toString(max));

        if (Math.abs(min - max) < 700 ) {
            return true;
        }
        return false;

    }



    public float getMax(ArrayList<Float> axs){

        float max = axs.get(0);

        for(int i =1; i<axs.size();i++){
            if(axs.get(i)> max){
                max = axs.get(i);
            }
        }

        return max;

    }


    public boolean isRight(ArrayList<Float> axs){

        int minindex=0;
        float min = axs.get(0);

        for(int i =1; i<axs.size();i++){

            if(axs.get(i)< min){
                min = axs.get(i);
                minindex = i;
            }
        }


        if (axs.size()==3 && minindex != 0 && minindex !=axs.size()-1) {
            return true;
        }

        return false;
    }


}
    
