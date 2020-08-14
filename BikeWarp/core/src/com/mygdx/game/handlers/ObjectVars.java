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
            float[] retsat = new float[] {622.40047f, 2.11474f, 611.38437f, 15.03484f, 588.13237f, 24.07394f, 497.65187f, 43.57424f, 383.77177f, 56.57424f, 299.53157f, 63.07424f, 260.53117f, 65.67404f, 243.37087f, 109.87424f, 218.41117f, 150.17454f, 177.85087f, 193.07464f, 131.05057f, 222.97484f, 78.25767f, 245.90184f, 32.99317f, 254.10174f, -10.63543f, 254.42184f, -53.26043f, 247.06174f, -93.96483f, 233.30144f, -131.67743f, 212.47264f, -165.69783f, 185.87224f, -195.09833f, 154.02164f, -218.67483f, 118.31294f, -235.09083f, 80.15224f, -239.84283f, 64.67204f, -293.22133f, 62.13024f, -426.32583f, 49.15024f, -499.95823f, 38.53024f, -555.18233f, 24.37024f, -580.67083f, 13.75024f, -590.58283f, 3.13004f, -586.33483f, -6.30976f, -556.59833f, -20.47006f, -499.95823f, -33.44996f, -433.40583f, -42.88996f, -298.88543f, -55.86996f, -243.65683f, -60.21856f, -235.34153f, -89.94296f, -218.46943f, -129.16316f, -196.77383f, -163.71756f, -165.02123f, -198.81796f, -116.42113f, -233.37846f, -69.11643f, -254.43856f, -36.06953f, -263.41846f, 5.09067f, -266.21856f, 53.08287f, -263.89826f, 104.49117f, -249.61796f, 153.13217f, -224.68046f, 199.14117f, -185.26006f, 237.99977f, -132.23956f, 259.00017f, -85.33916f, 264.88017f, -60.83896f, 339.35217f, -57.38216f, 432.97687f, -48.92196f, 554.44737f, -29.24326f, 583.73247f, -21.52326f, 613.27157f, -10.32306f};
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
