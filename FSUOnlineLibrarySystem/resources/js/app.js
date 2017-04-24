$(document).ready(function() {

    $("#submitBtn").on("click",function(){
      var principal = $("#InitialInvestment").val();
      var rateDay  = $("#InterestRate").val()/100;
      var depositMonth  = $("#Desposit").val();
      var begAge = $("#Started").val();

      var html ="";
      var yearNumber =0;
      var lastMonth =0;   
      var begBalYear =0;    
      var interestYear =0.0000;    
      var depositYear =0.00;   
      rateDay = rateDay/365;
      alert(rateDay);
      for ( var age = begAge*1+1; age <= 65; age++)
      {
        yearNumber = age - begAge;
        if ( yearNumber % 6 == 0) lastMonth = 13;
        begBalYear = principal;
        interestYear = 0;
        depositYear = 0;
        
        for ( var month = 1; month <= 12; month++)
        {
          for ( var day = 1; day <= 30; day++)
          {
            interestYear = interestYear*1+ Math.round(principal*rateDay*100) / 100;
            principal = principal*1 + (Math.round(principal*rateDay*100) / 100);
          }

            depositYear = depositYear*1 + depositMonth*1;
            principal = principal*1 + depositMonth*1;
        }
        
        for ( var day = 1; day <= 5; day++)
        {
          interestYear = interestYear*1 + Math.round(principal*rateDay*100) / 100;
          principal = principal*1 + Math.round(principal*rateDay*100) / 100;
        }

        if ( yearNumber % 6 == 0)
        {
          depositYear = depositYear+ 100;
          principal = principal + 100;
        }
        html+="<tr>"
               +"<td>"+ age +"</td>"
               +"<td>"+Number(begBalYear).toFixed(2)+"</td>"
               +"<td>"+interestYear.toFixed(2)+"</td>"
               +"<td>"+depositYear+"</td>"
               +"<td>"+principal.toFixed(2)+"</td>"
            +"</tr>;"
    }
      $("#res").html(html);

      return false;
    });

    
});
