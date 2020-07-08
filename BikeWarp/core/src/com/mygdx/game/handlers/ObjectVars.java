package com.mygdx.game.handlers;

public class ObjectVars {
	// Define the Indices
    public static final int Arrow = -1;
    public static final int Start = 0;
    public static final int Finish = 1;
    public static final int Gravity = 2;
    public static final int BallChain = 3;
    public static final int Boulder = 4;
    public static final int Crate = 5;
    public static final int DoorBlue = 6;
    public static final int DoorGreen = 7;
    public static final int DoorRed = 8;
    public static final int KeyBlue = 9;
    public static final int KeyGreen = 10;
    public static final int KeyRed = 11;
    public static final int Pendulum = 12;
    public static final int Spike = 13;
    public static final int Transport = 14;
    public static final int Jewel = 15;
    public static final int GateSwitch = 16;
    public static final int Bridge = 17;
    public static final int Log = 18;
    public static final int Nitrous = 19;
    public static final int TransportInvisible = 20;
    public static final int SpikeZone = 21;
    

    // Define the vertices
    public static final float[] objectArrow = {-1.0f,0.0f,-1.0f,-30.0f,-6.0f,-30.0f,0.0f,-40.0f,6.0f,-30.0f,1.0f,-30.0f,1.0f,0.0f};
    public static final float[] objectArrow1D = {-9.0f,-5.0f,0.0f,0.0f,-9.0f,5.0f};
    public static final float[] objectGravity = {0.0f,0.0f,20.1f,0.0f,17.4f,-10.0f,10.3f,-17.4f,0.0f,-20.3f,-10.0f,-17.6f,-17.6f,-10.0f,-20.1f,-0.2f,-17.6f,9.8f,-10.0f,17.4f,0.0f,20.3f,9.8f,17.6f,17.6f,9.8f,18.4f,7.3f,9.8f,7.1f,3.9f,11.5f,-3.7f,11.5f,-9.8f,7.1f,-12.2f,0.0f,-9.8f,-7.3f,-3.7f,-11.5f,3.7f,-11.8f,9.8f,-7.3f,0.0f,-7.3f};
    public static final float[] objectFinish = {14.3f,21.3f,-12.0f,21.3f,-12.0f,-19.3f,-3.7f,-19.3f,-3.7f,-3.3f,7.8f,-3.3f,7.8f,4.3f,-4.0f,4.3f,-4.0f,13.7f,14.3f,13.7f};
    public static final float[] objectFinishBall = {0.0f,0.0f,30.0f};
    public static final float[] objectStart = {14.12f,8.37f,12.03f,14.70f,7.13f,18.36f,0.73f,19.67f,-6.06f,18.75f,-11.41f,15.03f,-13.57f,9.68f,-11.74f,3.67f,-3.30f,-1.27f,6.37f,-4.00f,8.72f,-6.69f,8.73f,-9.95f,7.62f,-12.25f,3.02f,-14.74f,-3.90f,-14.55f,-8.02f,-12.14f,-9.85f,-8.87f,-10.30f,-6.39f,-15.07f,-6.91f,-14.29f,-10.83f,-11.15f,-15.99f,-5.99f,-18.73f,-0.44f,-19.84f,7.13f,-18.80f,12.49f,-15.47f,15.04f,-9.92f,14.84f,-5.15f,11.58f,-0.71f,6.29f,1.84f,-2.53f,4.06f,-7.02f,6.75f,-7.66f,11.05f,-5.14f,13.97f,0.21f,14.75f,6.34f,13.33f,8.90f,10.26f,9.29f,7.98f};
    public static final float[] objectStartWheels = {0.0f,0.0f,22.0f,100.0f,15.0f,50.0f,97.5f}; // left W x, left W y, wheel radius, wheel distance, head radius, head x, head y
    public static final float[] objectBallChain = {0.0f,-1000.0f,100.0f,-60.0f,-15.0f,120.0f,30.0f,1000.001f};
    public static final float[] objectBoulder = {0.0f,0.0f,200.0f};
    public static final float[] objectCrate = {-25.0f,-25.0f,25.0f,-25.0f,25.0f,25.0f,-25.0f,25.0f};
    public static final float[] objectDoor = {-3.32f,-85.0f,3.32f,-85.0f,3.32f,85.0f,-3.32f,85.0f};
    public static final float[] objectGateSwitch = {-3.32f,-85.0f,3.32f,-85.0f,3.32f,85.0f,-3.32f,85.0f,10.0f,-6.1f,40.0f,-6.1f,34.4f,6.1f,15.6f,6.1f,30.0f,0.0f};
//    public static final float[] objectKey = {-10.0f,0.0f,-8.66f,-2.5f,0.0f,-5.0f,8.66f,-2.5f,10.0f,0.0f,8.66f,2.5f,0.0f,5.0f,-8.66f,2.5f};
    public static final float[] objectKey = {-27.0f,-10.0f,27.0f,-10.0f,27.0f,10.0f,-27.0f,10.0f};
    public static final float[] objectSpike = {0.0f,0.0f,20.0f};
    public static final float[] objectTransport = {-150.0f,-20.0f,150.0f,-20.0f,150.0f,20.0f,-150.0f,20.0f,-150.0f,980.0f,150.0f,980.0f,150.0f,1020.0f,-150.0f,1020.0f};
//    public static final float[] objectJewel = {0.0f,0.0f,20.0f};
    public static final float[] objectJewel = {-15.0f,2.3f,0.0f,-13.0f,15.0f,2.3f,8.0f,8.0f,-8.0f,8.0f};
    public static final float[] objectBridge = {-15.0f,-15.0f,15.0f,-15.0f,15.0f,15.0f,-15.0f,15.0f,985.0f,-15.0f,1015.0f,-15.0f,1015.0f,15.0f,985.0f,15.0f,200.0f};
    public static final float[] objectLog = {0.0f,0.0f,25.0f};
    public static final float[] objectNitrous = {-30.0f,-15.0f,30.0f,-15.0f,30.0f,15.0f,-30.0f,15.0f};
    public static final float objectPadlock = 18.0f;
    public static final float objectTriggerWidth = 5.0f;
    public static final float[] objectSpikeZone = {-100.0f,-100.0f,100.0f,-100.0f,100.0f,100.0f,-100.0f,100.0f};
    
    // Define the global properties for some of the objects
    public static final float ChainLinkSize = 1.0f; // Size of a single link in a chain (in metres)
    //public static final float ChainLinkSize = 1.3f; // Size of a single link in a chain (in metres)

}
