package implement;

import informationExtractor.attrTypeNClass.Type;

public class Param {
	private String name;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	int cut;
	int digit;
	private Type type;
	private double initial;
	private double lowerBound;
	private double upperBound;
	private Strategy strategy;
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	public double getInitial() {
		return initial;
	}
	public void setInitial(double initial) {
		this.initial = initial;
	}
	public double getLowerBound() {
		return lowerBound;
	}
	public void setLowerBound(double lowerBound) {
		this.lowerBound = lowerBound;
	}
	public double getUpperBound() {
		return upperBound;
	}
	public void setUpperBound(double upperBound) {
		this.upperBound = upperBound;
	}
	public Strategy getStrategy() {
		return strategy;
	}
	public void setStrategy(Strategy strategy) {
		this.strategy = strategy;
	}
	public Param(double l,double u){
		this("",Type.INT,0,l,u,Strategy.selfTuning);
	}
	
	public Param(String n){
		this(n,Type.INT,0,Double.MIN_NORMAL,Double.MAX_VALUE,Strategy.selfTuning);
	}
	public Param(String n,Type t) {
		this(n,t,0,Double.MIN_NORMAL,Double.MAX_VALUE,Strategy.selfTuning);
	}
	public Param(String n,Type t,double i){
		this(n,t,i,Double.MIN_NORMAL,Double.MAX_VALUE,Strategy.selfTuning);
	}
	public Param(String n,Type t,double i,double l){
		this(n,t,i,l,Double.MAX_VALUE,Strategy.selfTuning);
	}
	public Param(String n,double i,double l,double u){
		this(n,Type.INT,i,l,u,Strategy.selfTuning);
	}
	public Param(String n,Type t,double i,double l,double u,int c,int d){
		this(n,t,i,l,u,Strategy.selfTuning);
		cut=c;
		this.digit=d;
	}
	public Param(String n,Type t,double i,double l,double u,int c){
		this(n,t,i,l,u,Strategy.selfTuning);
		cut=c;
	}
	public Param(String n,Type t,double i,double l,double u, Strategy s){
		name=n;
		type=t;
		initial=i;
		if(t==Type.INT){
			lowerBound=l>Integer.MIN_VALUE?l:Integer.MIN_VALUE;
			upperBound=u<Integer.MAX_VALUE?u:Integer.MAX_VALUE;
		}else {
			lowerBound=l>Double.MIN_VALUE?l:Double.MIN_VALUE;
			upperBound=u<Double.MAX_VALUE?u:Double.MAX_VALUE;
		}
		strategy=s;
	}
	public enum Strategy{selfTuning,BatchTuning,Auto}
}
