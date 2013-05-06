package com.schonherz.classes;

import java.text.Collator;
import java.util.ArrayList;
import java.util.List;


public class HungarianSorter {

	// This code here for Hungarian String comparation

	public String hungarianRules = ("< a,A < á,Á < b,B < c,C < cs,Cs < d,D < dz,Dz < dzs,Dzs "
			+ "< e,E < é,É < f,F < g,G < gy,Gy < h,H < i,I < í,Í < j,J "
			+ "< k,K < l,L < ly,Ly < m,M < n,N < ny,Ny < o,O < ó,Ó "
			+ "< ö,Ö < õ,Õ < p,P < q,Q < r,R < s,S < sz,Sz < t,T "
			+ "< ty,Ty < u,U < ú,Ú < v,V < w,W < x,X < y,Y < z,Z < zs,Zs");

	public static void sortStrings(Collator collator, List<String> words) {
		String tmp;
		for (int i = 0; i < words.size(); i++) {
			for (int j = i + 1; j < words.size(); j++) {
				if (collator.compare(words.get(i), words.get(j)) > 0) {
					tmp = words.get(i);
					words.set(i, words.get(j));
					words.set(j, tmp);
				}
			}
		}
	}
		
	

	public HungarianSorter() {
		super();
	}
	
}
