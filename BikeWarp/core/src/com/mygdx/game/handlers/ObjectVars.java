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
    public static final int GravityEarth = 22;
    public static final int GravityMoon = 23;
    public static final int GravityMars = 24;
    public static final int GravityZero = 25;
    public static final int TransportInvisibleEarth = 26;
    public static final int TransportInvisibleMars = 27;
    public static final int TransportInvisibleMoon = 28;
    public static final int TransportInvisibleZero = 29;
    public static final int PlanetSun = 30;
    public static final int PlanetMercury = 31;
    public static final int PlanetVenus = 32;
    public static final int PlanetEarth = 33;
    public static final int PlanetMars = 34;
    public static final int PlanetJupiter = 35;
    public static final int PlanetSaturn = 36;
    public static final int PlanetUranus = 37;
    public static final int PlanetNeptune = 38;

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

    public static boolean IsGravity(int dTyp) {
    	if ((dTyp == Gravity) | (dTyp == GravityEarth) | (dTyp == GravityMars) | (dTyp == GravityMoon) | (dTyp == GravityZero)) {
    		return true;
    	} else return false;
    }

    public static boolean IsTransportInvisible(int dTyp) {
    	if ((dTyp == TransportInvisible) | (dTyp == TransportInvisibleEarth) | (dTyp == TransportInvisibleMars) | (dTyp == TransportInvisibleMoon) | (dTyp == TransportInvisibleZero)) {
    		return true;
    	} else return false;
    }

    public static float[] MakePlanet(int dTyp, float xcen, float ycen) {
        if (dTyp == PlanetSun) {
            return new float[] {xcen, ycen, 628.41345f};
        } else if (dTyp == PlanetMercury) {
            return new float[] {xcen, ycen, 19.0f};
        } else if (dTyp == PlanetVenus) {
            return new float[] {xcen, ycen, 41.350018f};
        } else if (dTyp == PlanetEarth) {
            return new float[] {xcen, ycen, 40.488487f};
        } else if (dTyp == PlanetMars) {
            return new float[] {xcen, ycen, 26.898338f};
        } else if (dTyp == PlanetJupiter) {
            return new float[] {xcen, ycen, 320.4396f};
        } else if (dTyp == PlanetSaturn) {
            float[] retsat = new float[] {-0.55709f, 264.78696f, 49.39951f, 264.78696f, 91.31591f, 257.45676f, 130.88041f, 243.20176f, 154.62261f, 231.12656f, 182.79191f, 213.56306f, 202.41221f, 198.26256f, 225.60261f, 175.33726f, 245.04291f, 152.11176f, 265.83941f, 118.77616f, 278.72861f, 90.83946f, 287.87191f, 65.80896f, 324.46091f, 64.18876f, 387.67731f, 58.72976f, 456.88241f, 52.42976f, 538.62691f, 41.40976f, 574.12491f, 35.10776f, 601.23351f, 28.44736f, 626.50641f, 20.73736f, 642.20291f, 13.74716f, 651.54491f, 7.59216f, 657.02291f, 2.43196f, 657.64091f, -4.87324f, 648.04741f, -12.54324f, 634.11891f, -18.32124f, 606.51761f, -27.72134f, 567.39651f, -36.52164f, 504.72511f, -46.84364f, 394.13091f, -59.00404f, 320.03421f, -64.51894f, 292.19341f, -66.51894f, 287.07791f, -86.11054f, 275.20951f, -119.65084f, 259.21291f, -150.61124f, 245.79691f, -170.82174f, 224.35291f, -197.37004f, 203.13731f, -217.13024f, 183.16891f, -232.73044f, 154.80031f, -250.29144f, 126.71991f, -263.97164f, 95.18321f, -275.13174f, 64.94241f, -280.89204f, 37.29391f, -284.13204f, -2.35049f, -283.73384f, -47.95109f, -274.99384f, -85.60829f, -261.77024f, -112.60879f, -247.82004f, -138.52879f, -231.16984f, -157.96909f, -215.41964f, -176.86959f, -196.51944f, -201.93209f, -164.40124f, -221.02439f, -131.84074f, -233.01259f, -104.83024f, -239.67279f, -83.73994f, -244.55659f, -67.45964f, -315.90109f, -62.85054f, -397.21479f, -56.25044f, -465.51389f, -47.89324f, -521.14689f, -39.53324f, -556.71549f, -31.93304f, -582.94789f, -24.77094f, -595.06829f, -20.95084f, -606.68449f, -15.67054f, -617.19669f, -8.87054f, -621.46869f, -1.89024f, -619.06879f, 5.44976f, -612.61269f, 10.28976f, -603.66029f, 15.56986f, -582.15599f, 23.53006f, -548.67519f, 32.75036f, -515.73579f, 39.25906f, -482.33909f, 44.20906f, -442.07809f, 49.26916f, -387.92109f, 54.81596f, -313.61659f, 61.69616f, -259.95609f, 66.43376f, -239.36369f, 67.31356f, -230.38759f, 92.39376f, -216.65959f, 120.99446f, -203.45939f, 142.99476f, -189.20309f, 162.79496f, -172.32859f, 182.13476f, -150.05609f, 202.61526f, -129.31979f, 218.29546f, -108.19969f, 231.41576f, -85.60909f, 242.17906f, -61.24909f, 251.97906f, -40.24849f, 258.27936f, -20.08809f, 262.47936f};
            for (int ii=0; ii<retsat.length/2; ii++) {
                retsat[2*ii] += xcen;
                retsat[2*ii+1] += ycen;
            }
            return retsat;
        } else if (dTyp == PlanetUranus) {
            return new float[] {xcen, ycen, 115.536896f};
        } else if (dTyp == PlanetNeptune) {
            return new float[] {xcen, ycen, 115.536896f};
        }
        return new float[] {xcen, ycen, 100.0f};
    }

    public static boolean IsPlanet(int dTyp) {
        if ((dTyp >= PlanetSun) && (dTyp<=PlanetNeptune)) return true;
        return false;
    }

}
