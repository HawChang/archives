library(TSA)
findPQ<-function(data){
	findPQ.s<-eacf(data,ar.max=15,ma.max=15)
	findPQ.table<-findPQ.s$symbol
	candidates<-as.data.frame(matrix(numeric(0),ncol=4))
	for(i in 16:1){
		for(j in 16:1){
			if(findPQ.table[i,j]=="o"){
				if(i<=j){
					candidates<-rbind(candidates,c(i-1,j-1,abs(i-j),(17-j)*(18-j)/2))
				}else{
					candidates<-rbind(candidates,c(i-1,j-1,abs(i-j),(17-i)*(18+i-2*j)/2))
				}
			}else if(findPQ.table[i,j]=="x"){
				for(m in i:1){
					for(n in j:1){
						findPQ.table[m,n]<-"r";
					}
					j=j-1
				}
				break
			}else if(findPQ.table[i,j]=="r"){
				break
			}
		}
	}
	names(candidates)<-c("p","q","distance","area")
	candidates<-candidates[order(-candidates[,"area"],candidates[,"distance"]),]
	return(list(p=candidates[1,]$p,q=candidates[1,]$q,candidates=candidates))
}