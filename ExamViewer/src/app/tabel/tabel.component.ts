import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-tabel',
  templateUrl: './tabel.component.html',
  styleUrls: ['./tabel.component.css']
})
export class TabelComponent implements OnInit {


 arr : string = "";
 ExamArray  ;

  ngOnInit() {
  }

  public fileString;

  constructor() {
  }

  changeListener($event): void {
    this.readThis($event.target);
  }

  readThis(inputValue: any): void {
    var file: File = inputValue.files[0];
    var myReader: FileReader = new FileReader();
    var fileType = inputValue.parentElement.id;
    myReader.onload = (e) => {
      //console.log(myReader.result);
      this.fileString = myReader.result;
      this.arr = myReader.result;


      this.ExamArray = [];
      let DaysArray = this.arr.split('\n');

      for (let i = 0 ; i < DaysArray.length ; i++){
          this.ExamArray[i]  =  DaysArray[i].split(" ");
      }


    };


   myReader.readAsText(file);
   // console.log(this.arr)


  }
}
