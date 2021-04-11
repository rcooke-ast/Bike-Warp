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
    public static final int DoorKey = 39;
    public static final int JewelBG = 40;
    public static final int JewelFG = 41;
    public static final int UFO = 42;
    public static final int TransportSilentEarth = 43;
    public static final int TransportSilentMars = 44;
    public static final int TransportSilentMoon = 45;
    public static final int TransportSilentZero = 46;
    public static final int TransportSilent = 47;
    public static final int MoveableSign = 48;
    // These numbers must not exceed 100, due to signs

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
    public static final float[] objectUFO = {-500.0f, -110.61588330632091f, 500.0f, -110.61588330632091f, 500.0f, 110.61588330632091f, -500.0f, 110.61588330632091f};
    public static final float[] objectCircleRoadSign = {0.0f,0.0f,30.0f,0.0f};

    // Define the global properties for some of the objects
    public static final float ChainLinkSize = 1.0f; // Size of a single link in a chain (in metres)
    //public static final float ChainLinkSize = 1.3f; // Size of a single link in a chain (in metres)

    public static boolean IsDoor(int dTyp) {
        if ((dTyp == DoorBlue) | (dTyp == DoorGreen) | (dTyp == DoorRed)) {
            return true;
        } else return false;
    }

    public static boolean IsKey(int dTyp) {
        if ((dTyp == KeyBlue) | (dTyp == KeyGreen) | (dTyp == KeyRed)) {
            return true;
        } else return false;
    }

    public static boolean IsMoveableSign(int dTyp) {
        return DecorVars.IsRoadSign(dTyp-100);
    }

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

    public static boolean IsTransportSilent(int dTyp) {
        if ((dTyp == TransportSilent) | (dTyp == TransportSilentEarth) | (dTyp == TransportSilentMars) | (dTyp == TransportSilentMoon) | (dTyp == TransportSilentZero)) {
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
            float[] retsat = new float[] {-629.70020f, 8.46190f, -622.13990f, 20.64200f, -595.42770f, 32.40210f, -539.98700f, 46.68230f, -459.37230f, 59.08200f, -361.93160f, 68.36200f, -266.57860f, 74.16210f, -252.36670f, 108.85710f, -228.51030f, 151.97800f, -204.24900f, 180.99630f, -167.96030f, 213.39660f, -127.52680f, 238.48700f, -87.79430f, 255.25730f, -45.48190f, 265.57730f, -11.42530f, 268.15740f, 36.37500f, 264.90620f, 76.19170f, 255.66620f, 112.48000f, 240.96600f, 138.68870f, 226.26580f, 173.46470f, 200.64550f, 197.82030f, 175.86260f, 220.50070f, 146.46210f, 240.86470f, 109.11790f, 252.86470f, 76.11740f, 362.66600f, 68.61740f, 463.00070f, 58.05780f, 552.85740f, 43.65740f, 587.41170f, 34.53660f, 618.13170f, 22.37640f, 629.70020f, 8.95630f, 619.08010f, -3.10280f, 590.25870f, -15.38580f, 513.36170f, -33.02590f, 441.64940f, -42.42410f, 320.59230f, -53.86440f, 257.98670f, -56.92560f, 250.33940f, -84.73640f, 231.73930f, -128.23690f, 213.73870f, -156.73710f, 187.33870f, -187.23740f, 162.65140f, -209.64680f, 121.37070f, -236.44740f, 77.21000f, -255.24740f, 42.08170f, -264.46740f, -4.65870f, -268.15740f, -57.85300f, -263.16680f, -105.32530f, -249.36640f, -141.75780f, -232.34620f, -176.17190f, -208.52960f, -200.28270f, -185.14840f, -226.32270f, -155.39800f, -244.38330f, -124.94760f, -259.86030f, -92.59570f, -270.36030f, -56.54520f, -359.14630f, -51.19390f, -447.14890f, -42.71760f, -532.97390f, -29.75740f, -582.04900f, -18.45590f, -604.36960f, -11.25590f, -623.44990f, -1.35580f};
                    //-613.26905f, -5.83045f, -601.25705f, 18.73955f, -565.22095f, 45.12975f, -473.49225f, 76.06985f, -386.13185f, 94.27015f, -304.23125f, 105.19025f, -255.09125f, 109.74005f, -236.72755f, 141.87535f, -203.99125f, 182.79585f, -173.48685f, 211.93585f, -137.77425f, 236.73615f, -101.31825f, 254.09625f, -59.57665f, 265.59705f, -13.97625f, 271.19715f, 26.34775f, 267.15515f, 61.20795f, 257.70485f, 88.50875f, 247.55485f, 122.60975f, 229.13685f, 155.44235f, 206.09655f, 199.07515f, 165.05595f, 222.83545f, 135.17535f, 240.00825f, 109.09315f, 331.62205f, 93.10705f, 422.27875f, 76.54355f, 496.19925f, 56.54325f, 557.63975f, 33.34335f, 596.03975f, 11.74305f, 607.94335f, -0.41445f, 613.26905f, -15.70185f, 607.87645f, -31.50965f, 595.97215f, -45.14985f, 566.21175f, -63.13015f, 497.01975f, -87.31015f, 431.54675f, -100.33015f, 375.48825f, -108.64375f, 314.43175f, -113.92375f, 236.09475f, -120.64395f, 220.72215f, -146.89295f, 193.18175f, -184.69355f, 165.64175f, -210.79385f, 126.40815f, -235.61515f, 90.87575f, -253.00525f, 47.44725f, -265.22545f, -7.58525f, -271.19715f, -56.29425f, -265.91715f, -98.28125f, -253.98015f, -141.79395f, -234.36975f, -192.20625f, -196.24995f, -227.60625f, -155.74945f, -251.54055f, -114.58875f, -294.73225f, -111.21115f, -360.28855f, -102.85175f, -439.77725f, -90.43155f, -506.95335f, -74.43715f, -545.95365f, -60.93715f, -579.55425f, -44.43695f, -599.35425f, -30.93695f, -609.55445f, -17.93665f};
                    //606.49165f, 8.01310f, 595.47555f, 20.93320f, 572.22355f, 29.97230f, 481.74305f, 49.47260f, 367.86295f, 62.47260f, 283.62275f, 68.97260f, 244.62235f, 71.57240f, 227.46205f, 115.77260f, 202.50235f, 156.07290f, 161.94205f, 198.97300f, 115.14175f, 228.87320f, 62.34885f, 251.80020f, 17.08435f, 260.00010f, -26.54425f, 260.32020f, -69.16925f, 252.96010f, -109.87365f, 239.19980f, -147.58625f, 218.37100f, -181.60665f, 191.77060f, -211.00715f, 159.92000f, -234.58365f, 124.21130f, -250.99965f, 86.05060f, -255.75165f, 70.57040f, -309.13015f, 68.02860f, -442.23465f, 55.04860f, -515.86705f, 44.42860f, -571.09115f, 30.26860f, -596.57965f, 19.64860f, -606.49165f, 9.02840f, -602.24365f, -0.41140f, -572.50715f, -14.57170f, -515.86705f, -27.55160f, -449.31465f, -36.99160f, -314.79425f, -49.97160f, -259.56565f, -54.32020f, -251.25035f, -84.04460f, -234.37825f, -123.26480f, -212.68265f, -157.81920f, -180.93005f, -192.91960f, -132.32995f, -227.48010f, -85.02525f, -248.54020f, -51.97835f, -257.52010f, -10.81815f, -260.32020f, 37.17405f, -257.99990f, 88.58235f, -243.71960f, 137.22335f, -218.78210f, 183.23235f, -179.36170f, 222.09095f, -126.34120f, 243.09135f, -79.44080f, 248.97135f, -54.94060f, 323.44335f, -51.48380f, 417.06805f, -43.02360f, 538.53855f, -23.34490f, 567.82365f, -15.62490f, 597.36275f, -4.42470f};
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

    public static boolean IsEmerald(int dTyp) {
        if ((dTyp == Jewel) | (dTyp == JewelBG) | (dTyp == JewelFG)) return true;
        return false;
    }

    public static int GetSaturnMinMax(int xy, int minmax) {
        if ((xy==0) && (minmax==0)) return 0;
        if ((xy==0) && (minmax==1)) return 58;
        if ((xy==1) && (minmax==0)) return 89;
        if ((xy==1) && (minmax==1)) return 29;
//        if ((xy==0) && (minmax==0)) return 0;
//        if ((xy==0) && (minmax==1)) return 56;
//        if ((xy==1) && (minmax==0)) return 87;
//        if ((xy==1) && (minmax==1)) return 27;
//        if ((xy==0) && (minmax==0)) return 0;
//        if ((xy==0) && (minmax==1)) return 54;
//        if ((xy==1) && (minmax==0)) return 83;
//        if ((xy==1) && (minmax==1)) return 27;
        return -1;
    }

}
