

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Random;
import javax.swing.ImageIcon;

public class GlobalMapGenerator{
	private int[][] fkz;
	private int[] bprx = new int[64];
	private int[] bpry = new int[64];
	private int xxx;
	private int yyy;
	private BufferedImage bio;
	private static int SeaBlue = new Color(14, 17, 50).getRGB();
	private static int GrassGreen = new Color(68, 73, 43).getRGB();
	private static int SandYellow = new Color(221, 206, 187).getRGB();
	private static int SnowWhite = new Color(240, 243, 252).getRGB();
	private static int ForestGreen = new Color(35, 59, 27).getRGB();
	private Random r = new Random();
	private static GlobalMapGenerator gmg;
	public GlobalMapGenerator(){
		for(int i = 0; i < this.bprx.length; i++){
			this.bprx[i] = -1;
			this.bpry[i] = -1;
		}
	}
	public ImageIcon generate(int xx, int yy, int ldr){
		ImageIcon iio = new ImageIcon();
		iio = new ImageIcon(this.generateBuffered(xx, yy, ldr));
		return iio;
	}
	public BufferedImage generateBuffered(int xx, int yy, int ldr){
		this.xxx = xx - 1;
		this.yyy = yy - 1;
		this.fkz = new int[xx][yy];

		this.bio = new BufferedImage(xx, yy, BufferedImage.TYPE_INT_ARGB);
		for(int x = 0; x < xx; x++){
			for(int y = 0; y < yy; y++){
				this.fkz[x][y] = (r.nextInt(25) < ldr) ? 1 : 0;
			}
		}	
		this.sortMap(10, xx, yy);
		this.paintBuffered(xx, yy);
		return this.bio;
	}
	public BufferedImage generateSeaBuffered(int xx, int yy){
		this.xxx = xx - 1;
		this.yyy = yy - 1;
		this.fkz = new int[xx][yy];

		this.bio = new BufferedImage(xx, yy, BufferedImage.TYPE_INT_ARGB);
		for(int x = 0; x < xx; x++){
			for(int y = 0; y < yy; y++){
				this.fkz[x][y] = 0;
			}
		}	
		this.paintBuffered(xx, yy);
		return this.bio;
	}
	public BufferedImage generateWithLandBuffered(int xx, int yy, int dls){
		GlobalMapGenerator gmgc = new GlobalMapGenerator();
		BufferedImage bi1 = gmgc.generateSeaBuffered(xx, yy);
		for(int c = 0; c < dls; c++){
			BufferedImage bi2 = gmgc.randLandBuffered(2, 40);
			bi2 = IncreaseBuffer.execute1(bi2, (xx / 60));
			int gxx = bi2.getWidth();
			int gyy = bi2.getHeight();
			GlobalMapGenerator gmg1 = new GlobalMapGenerator();
			gmg1.setBufferedImage(bi2, gxx, gyy);
			gmg1.inputFKZArray(gxx, gyy);
			gmg1.sortLandMap(15, gxx, gyy);
			gmg1.paintBuffered(gxx, gyy);
			bi2 = gmg1.bio;
			int rx = r.nextInt(xx);
			int ry = r.nextInt(yy);
			for(int x = 0; x < bi2.getWidth(); x++){
				for(int y = 0; y < bi2.getHeight(); y++){
					if(bi2.getRGB(x, y) != SeaBlue){
						int fx = (((x + rx) < xx) ? (x + rx) : (x + rx - xx));
						int fy = (((y + ry) < yy) ? (y + ry) : (y + ry - yy));
						bi1.setRGB(fx, fy, bi2.getRGB(x, y));
					}
				}
			}
		}
		gmgc.setBufferedImage(bi1, xx, yy);
		gmgc.inputFKZArray(xx, yy);
		gmgc.smoothExplosion(5, xx, yy, 0, 1, 10);
		gmgc.sortMap(10, xx, yy);
		gmgc.paintBuffered(xx, yy);

		this.generateBiome(SandYellow, 380, 2, 2, xx, yy, gmgc);
		this.generateBiome(SnowWhite, 310, 1, 2, xx, yy, gmgc);
		this.generateBiome(ForestGreen, 320, 1, 1, xx, yy, gmgc);
		
		gmgc.generateSandInLandNearSea(xx, yy);
		bi1 = gmgc.bio;
		return bi1;
	}
	public void generateBiome(int rgb, int rou, int jk, int rk, int xx, int yy, GlobalMapGenerator gmg){
		gmg.generateBiomeInLand(xx, yy, jk + r.nextInt(rk + 1), rou, rgb);
		gmg.sortBiomeMap(3, xx, yy, rgb);
		gmg.paintBuffered(xx, yy);
	}
	public void generateBiomeInLand(int xx, int yy, int bcs, int rou, int biome){
		int rd = xx / 3;
		int rdo = rd / 2;
		for(int c = 0; c < bcs; c++){
			int jx, jy;
			while(true){
				jx = r.nextInt(xx);
				jy = r.nextInt(yy);
				boolean flag = true;
				for(int i = 0; i < this.bprx.length; i ++){
					if(this.bprx[i] == -1){
						break;
					}
					if(scpfh(this.bprx[i], this.bpry[i], jx, jy, xx, yy) <= (pf(rdo))){
						flag = false;
					}
				}
				if(this.getRGBWithoutBound(jx, jy, xx, yy) != biome && this.getRGBWithoutBound(jx, jy, xx, yy) != SeaBlue && flag){
					int slot = this.getNextUseableRegisterSlot();
					this.bprx[slot] = jx;
					this.bpry[slot] = jy;
					break;
				}
			}
			GlobalMapGenerator gmgg = new GlobalMapGenerator();
			BufferedImage bi2 = gmgg.randLandBuffered(2, rdo / 2);
			bi2 = IncreaseBuffer.execute1(bi2, 4);
			int gxx = bi2.getWidth();
			int gyy = bi2.getHeight();
			gmgg.setBufferedImage(bi2, gxx, gyy);
			gmgg.inputFKZArray(gxx, gyy);
			gmgg.paintBuffered(gxx, gyy);
			bi2 = gmgg.bio;
			for(int x = -rdo; x < rdo; x++){
				for(int y = -rdo; y < rdo; y++){
					int pfh = pfh(x, y);
					int rad = (pfh == 0 ? 0 : (r.nextInt(pfh)));
					int bm = bi2.getRGB(x + rdo, y + rdo);
					if(bm != SeaBlue && ((pfh * rou / 1000) - rad >= 0)){
						int jbm = this.getRGBWithoutBound(jx + x, jy + y, xx, yy);
						if(jbm != biome && jbm != SeaBlue){
							this.setRGBWithoutBound(jx + x, jy + y, xx, yy, biome);
						}
					}
				}
			}
		}
		for(int x = 0; x < xx; x++){
			for(int y = 0; y < yy; y++){
				if(this.getRGBWithoutBound(x, y, xx, yy) != biome && this.getRGBWithoutBound(x, y, xx, yy) != SeaBlue
						&& ((this.getSurroundingFKZCount(x, y, xx, yy, getFKZnumFromRGB(biome)) > 0 && r.nextInt(100) < 40))){
					this.setRGBWithoutBound(x, y, xx, yy, biome);
				}
			}
		}
	}
	public int getSurroundingFKZCount(int x, int y, int xx, int yy, int fkz){
		int s = 0;
		if(getFKZnumFromRGB(this.getRGBWithoutBound(x + 1, y, xx, yy)) == fkz) s++;
		if(getFKZnumFromRGB(this.getRGBWithoutBound(x - 1, y, xx, yy)) == fkz) s++;
		if(getFKZnumFromRGB(this.getRGBWithoutBound(x, y + 1, xx, yy)) == fkz) s++;
		if(getFKZnumFromRGB(this.getRGBWithoutBound(x, y - 1, xx, yy)) == fkz) s++;
		return s;
	}
	public void generateSandInLandNearSea(int xx, int yy){
		for(int cx = 0; cx < xx; cx++){
			for(int cy = 0; cy < yy; cy++){
				int x1 = (cx + 1) % xx;
				int x2 = (cx - 1) < 0 ? (xx + cx - 1) : (cx - 1);
				int y1 = (cy + 1) % yy;
				int y2 = (cy - 1) < 0 ? (yy + cy - 1) : (cy - 1);
				if(this.fkz[cx][cy] != 2 && this.fkz[cx][cy] != 0 && (this.fkz[x1][cy] == 0 || this.fkz[x2][cy] == 0 || this.fkz[cx][y1] == 0 || this.fkz[cx][y2] == 0)){
					int rd = r.nextInt(2);
					for(int jx = -rd; jx < (rd > 0 ? rd : 1); jx++){
						for(int jy = -rd; jy < (rd > 0 ? rd : 1); jy++){
							int bm = this.getRGBWithoutBound(cx + jx, cy + jy, xx, yy);
							if(bm != SandYellow && bm != SeaBlue){
								this.setRGBWithoutBound(cx + jx, cy + jy, xx, yy, SandYellow);
							}
						}
					}
				}
			}
		}
	}
	public int getSurroundRandomBiome(int x, int y, int xx, int yy){
		int x1 = this.getRGBWithoutBound(x + 1, y, xx, yy);
		int x2 = this.getRGBWithoutBound(x - 1, y, xx, yy);
		int x3 = this.getRGBWithoutBound(x, y + 1, xx, yy);
		int x4 = this.getRGBWithoutBound(x, y - 1, xx, yy);
		int rs = r.nextInt(4);
		return (rs == 0) ? x1 : ((rs == 1) ? x2 : ((rs == 2) ? x3 : x4));
	}
	public int getFKZArrayWithoutBound(int x, int y, int xx, int yy){
		if(x < 0 || y < 0){
			return -1;
		}
		x %= xx;
		y %= yy;
		return this.fkz[x][y];
	}
	public void setFKZArrayWithoutBound(int x, int y, int xx, int yy, int set){
		x = (x < 0) ? (xx + x) : (x % xx);
		y = (y < 0) ? (yy + y) : (y % yy);
		this.fkz[x][y] = set;
	}
	public void setRGBWithoutBound(int x, int y, int xx, int yy, int rgb){
		if(x < 0 || y < 0){
			return;
		}
		x %= xx;
		y %= yy;
		this.bio.setRGB(x, y, rgb);
		this.setFKZArrayWithoutBound(x, y, xx, yy, getFKZnumFromRGB(rgb));
	}
	public int getRGBWithoutBound(int x, int y, int xx, int yy){
		if(x < 0 || y < 0){
			return -1;
		}
		x %= xx;
		y %= yy;
		return this.bio.getRGB(x, y);
	}
	public ImageIcon generateWithLand(int xx, int yy, int dls){
		ImageIcon i = new ImageIcon(generateWithLandBuffered(xx, yy, dls));
		return i;
	}
	public void inputFKZArray(int xx, int yy){
		fkz = new int[xx][yy];
		for(int x = 0; x < xx; x++){
			for(int y = 0; y < yy; y++){
				fkz[x][y] = getFKZnumFromRGB(this.bio.getRGB(x, y));
			}
		}
	}
	public void setBufferedImage(BufferedImage bi, int x, int y){
		this.bio = bi;
		this.xxx = x - 1;
		this.yyy = y - 1;
	}
	public void paintBuffered(int xx, int yy){
		for(int x = 0; x < xx; x++){
			for(int y = 0; y < yy; y++){
				a:switch(this.fkz[x][y]){
					case 0: this.setSea(x, y); break a;
					case 1: this.setGrass(x, y); break a;
					case 2: this.setSand(x, y); break a;
					case 3: this.setSnow(x, y); break a;
					case 4: this.setForest(x, y); break a;
				}
			}
		}
	}
	public void sortMap(int cs, int xx, int yy){
		for(int c = 0; c < cs; c++){
			for(int x = 0; x < xx; x++){
				for(int y = 0; y < yy; y++){
					if(this.fkz[x][y] == 0){
						if(this.getNeighborBlockCount(x, y, 0) < 4){
							this.fkz[x][y] = 1;
						}
					}
					if(this.fkz[x][y] == 1){
						if(this.getNeighborBlockCount(x, y, 0) >= 5){
							this.fkz[x][y] = 0;
						}
					}
				}	
			}
		}
	}
	public void sortLandMap(int cs, int xx, int yy){
		for(int c = 0; c < cs; c++){
			for(int x = 0; x < xx; x++){
				for(int y = 0; y < yy; y++){
					if(this.fkz[x][y] != 0){
						int ns = this.getNeighborBlockCount(x, y, 0);
						if(ns == 3 || ns == 4 && r.nextInt(8) < 7){
							this.fkz[x][y] = 0;
						}
					}
				}	
			}
		}
	}
	public void sortBiomeMap(int cs, int xx, int yy, int l){
		for(int c = 0; c < cs; c++){
			for(int x = 0; x < xx; x++){
				for(int y = 0; y < yy; y++){
					int bg = this.getRGBWithoutBound(x, y, xx, yy);
					int nsb = this.getNeighborBlockCount(x, y, getFKZnumFromRGB(l));
					if(bg != l && bg != SeaBlue){
						if(8 - nsb < 4){
							this.setRGBWithoutBound(x, y, xx, yy, l);
						}
					}
					if(bg == l){
						if(8 - nsb >= 5){
							this.setRGBWithoutBound(x, y, xx, yy, this.getSurroundRandomBiome(x, y, xx, yy));
						}
					}
				}
			}
		}
	}
	public void smoothExplosion(int cs, int xx, int yy, int h, int l, int js){
		for(int c = 0; c < cs; c++){
			for(int x = 0; x < xx; x++){
				for(int y = 0; y < yy; y++){
					int ns = this.getNeighborBlockCount(x, y, h);
					int rr = 3 + r.nextInt(6);
					if(this.fkz[x][y] == l){
						if(ns == 3 && r.nextInt(100) < js){
							for(int m = -rr; m < rr; m++){
								for(int n = -rr; n < rr; n++){
									if(pfh(m, n) < (18 + r.nextInt(81))){
										int ix = (x + m < 0) ? xx + x + m - 1 : x + m;
										int iy = (y + n < 0) ? yy + y + n - 1 : y + n;
										ix = (ix < xx) ? ix : ix - xx;
										iy = (iy < yy) ? iy : iy - yy;
										if(this.fkz[ix][iy] == l){
											this.fkz[ix][iy] = h;
										}
									}
								}
							}
						}
					}
				}	
			}
		}
	}
	public void setSea(int x, int y){
		this.bio.setRGB(x, y, SeaBlue);
		this.fkz[x][y] = 0;
	}
	public void setGrass(int x, int y){
		this.bio.setRGB(x, y, GrassGreen);
		this.fkz[x][y] = 1;
	}
	public void setSand(int x, int y){
		this.bio.setRGB(x, y, SandYellow);
		this.fkz[x][y] = 2;
	}
	public void setSnow(int x, int y){
		this.bio.setRGB(x, y, SnowWhite);
		this.fkz[x][y] = 3;
	}
	public void setForest(int x, int y){
		this.bio.setRGB(x, y, ForestGreen);
		this.fkz[x][y] = 4;
	}
	public static int getFKZnumFromRGB(int rgb){
		if(rgb == SeaBlue) return 0;
		if(rgb == GrassGreen) return 1;
		if(rgb == SandYellow) return 2;
		if(rgb == SnowWhite) return 3;
		if(rgb == ForestGreen) return 4;
		return -1;
	}
	public BufferedImage randLandBuffered(int frcs, int bc){
		if(frcs < 1 || frcs > 5){
			return null;
		}
		gmg = new GlobalMapGenerator();
		BufferedImage i = gmg.generateBuffered(bc, bc, 14);
		for(int m = 0; m < frcs - 1; m++){
			BufferedImage ix = gmg.generateBuffered(bc, bc, 14);
			BufferedImage bio = new BufferedImage(bc, bc, BufferedImage.TYPE_INT_ARGB);
			for(int x = 0; x < bio.getWidth(); x++){
				for(int y = 0; y < bio.getHeight(); y++){
					bio.setRGB(x, y, SeaBlue);
				}
			}
			for(int o = 0; o < bc; o++){
				for(int oo = 0; oo < bc; oo++){
					if(i.getRGB(o, oo) == GrassGreen && ix.getRGB(o, oo) == GrassGreen && pfh(o - bc / 2, oo - bc / 2) <= (pf(bc / 2) / 4 + this.r.nextInt(pf(bc / 2) * 3 / 4))){
						bio.setRGB(o, oo, GrassGreen);
					}
				}
			}
			i = bio;
		}
		GlobalMapGenerator gmg1 = new GlobalMapGenerator();
		gmg1.setBufferedImage(i, bc, bc);
		gmg1.inputFKZArray(bc, bc);
		gmg1.sortMap(6, bc, bc);
		gmg1.paintBuffered(bc, bc);
		i = gmg1.bio;
		return i;
	}
	public ImageIcon randLand(int frcs, int bc){
		ImageIcon i = new ImageIcon(randLandBuffered(frcs, bc));
		return i;
	}
	public int getNeighborBlockCount(int x, int y, int fkz){
		if(x > this.xxx || y > this.yyy || x < 0 || y < 0){
			return -1;
		}
		int[] hori = {x - 1, x, x + 1};
		int[] vert = {y - 1, y, y + 1};
		if(x == 0){
			hori[0] = this.xxx;
		}
		if(y == 0){
			vert[0] = this.yyy;
		}
		if(x == this.xxx){
			hori[2] = 0;
		}
		if(y == this.yyy){
			vert[2] = 0;
		}
		int sbc = 0;
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				if(i == 1 && j == 1){
					continue;
				}
				if(this.fkz[hori[i]][vert[j]] == fkz){
					sbc++;
				}
			}
		}
		return sbc;
	}
	public int getNextUseableRegisterSlot(){
		int s = 0;
		for(int i = 0; i < this.bprx.length; i++){
			if(this.bprx[i] == -1){
				s = i;
				break;
			}
		}
		return s;
	}
	public static int scpfh(int x1, int y1, int x2, int y2, int xx, int yy){
		int dx = (jdz(x2 - x1) > (xx / 2)) ? jdz((xx / 2) - jdz(x2 - x1)) : (x2 - x1);
		int dy = (jdz(y2 - y1) > (yy / 2)) ? jdz((yy / 2) - jdz(y2 - y1)) : (y2 - y1);
		return pf(dx) + pf(dy);
	}
	public static int jdz(int s){
		return s < 0 ? -s : s;
	}
	public static int pfh(int x, int y){
		return pf(x) + pf(y);
	}
	public static int pf(int x){
		return x * x;
	}
}
