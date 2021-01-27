import { Component, OnInit } from '@angular/core';
import {PageEvent} from "@angular/material/paginator";
import {EmployeeDto, EmployeeService, PageEmployeeDto} from "../../../generated";
import {Observable} from "rxjs";
import {MatDialog} from "@angular/material/dialog";
import {ConfirmationDialogComponent, ConfirmDialogModel} from "../confirmation-dialog/confirmation-dialog.component";
import {Router} from "@angular/router";
import {TokenStorageService} from "../_services/token-storage.service";

@Component({
  selector: 'app-employee-list',
  templateUrl: './employee-list.component.html',
  styleUrls: ['./employee-list.component.css']
})
export class EmployeeListComponent implements OnInit {
  pageNum: number = 0;
  pageSize: number = 10;
  pageSizeOptions = [5, 10, 25];
  showFirstLastButtons = true;
  lenght: number;
  _empl: Observable<PageEmployeeDto>;
  employeeList: Array<EmployeeDto>;
  excludeId: number;
  constructor(private service: EmployeeService,
              public dialog: MatDialog,
              private router:Router,
              private tokenStorageService: TokenStorageService) {
    this.excludeId = Number.parseInt(tokenStorageService.getUser().id)
  }

  ngOnInit(): void {
    this.loadEmployees()
  }

  handlePageEvent(event: PageEvent) {
    this.pageSize = event.pageSize;
    this.pageNum = event.pageIndex;
    this.loadEmployees();
  }

  loadEmployees(){
   this._empl =  this.service.getAllEmployees(this.pageNum,this.pageSize,this.excludeId);
    this._empl.subscribe(value => {
      value.content = this.employeeList;
      this.lenght = value.totalElements
    })
  }

  deleteEmployeeOnPressButton(id:number){
    const message = 'Вы действительно хотите удалить сотрудника?';
    const dialogData = new ConfirmDialogModel("Подтверждение", message);
    const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
      data: dialogData
    });
    dialogRef.afterClosed().subscribe(dialogResult => {
      if(dialogResult){
        this.service.deleteEmployeeById(id).subscribe(value => this.loadEmployees());
      }
    });
  }

  editVacationOnEmployee(id:number){
    sessionStorage.setItem("id",id.toString());
    this.router.navigateByUrl('/edit');
  }

}
