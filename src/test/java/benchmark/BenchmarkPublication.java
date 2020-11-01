package benchmark;

import de.embl.cba.bdp2.BigDataProcessor2;
import de.embl.cba.bdp2.image.Image;
import de.embl.cba.bdp2.log.Logger;
import de.embl.cba.bdp2.save.SaveFileType;
import de.embl.cba.bdp2.save.SavingSettings;

public class BenchmarkPublication
{
	public static void main( String[] args )
	{
		Logger.setLevel( Logger.Level.Normal );
//		String root = "/Users/tischer/Desktop/bpd2-benchmark";
		String root = "/Volumes/cba/exchange/bigdataprocessor/data/benchmark";

		Image image = BigDataProcessor2.openHdf5Series(
				root + "/in",
				".*stack_6_(?<C1>channel_.*)/(?<C2>Cam_.*)_(?<T>\\d+).h5",
				"Data" );


		//image = BigDataProcessor2.bin( image, new long[]{3,3,1,1,1} );

		BigDataProcessor2.showImage( image );

//		SavingSettings savingSettings = new SavingSettings();
//		savingSettings.volumesFilePathStump = root + "/out/volumes";
//		savingSettings.numIOThreads = 1;
//		savingSettings.numProcessingThreads = 4;
//		savingSettings.fileType = SaveFileType.TiffVolumes;
//		savingSettings.saveProjections = false;
//		savingSettings.saveVolumes = true;
//		savingSettings.compression = savingSettings.COMPRESSION_NONE;
//		savingSettings.tStart = 0;
//		savingSettings.tEnd = 9;
//		BigDataProcessor2.saveImageAndWaitUntilDone( image, savingSettings );

	}
}
