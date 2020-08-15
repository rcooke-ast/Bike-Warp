rm BikeWarp/core/assets/BGstate.dat
rm BikeWarp/core/assets/WorldRecords.dat
rm BikeWarp/core/assets/replays/*
rmdir BikeWarp/core/assets/replays
cp BikeWarp/desktop/build/libs/desktop-1.0.jar BikeWarp.jar
git commit * -m "update"
git push
cp BikeWarp.jar ~/Desktop/BikeWarp/
