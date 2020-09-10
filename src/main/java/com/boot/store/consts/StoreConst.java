package com.boot.store.consts;

import java.io.Serializable;

/**
 * @Author：chenaiwei
 * @Description：CacheConst
 * @CreateDate：2020/7/8 18:03
 */
public class StoreConst implements Serializable {
	public static final String PROJECT = "store";
	public static final String VERIFY_CODE = "blend";
	public static final String VERIFY_NUM_CODE = "number";

	public static final String LETTER_STR =  "a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z,A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z";
	public static final String NUMBER_STR = "0,1,2,3,4,5,6,7,8,9";
	public static final String DB_READ_ONLY = "Connection is read-only";

	public static final Integer MEMBER_MONEY_CHARGE = 1;
	public static final Integer MEMBER_MONEY_CONSUME = 2;

	public static final Integer PURCHASE_SELL = 1;
	public static final Integer PURCHASE_INTO = 2;

	public static final Integer STOCK_ADD = 1;
	public static final Integer STOCK_SUB = 2;


}
