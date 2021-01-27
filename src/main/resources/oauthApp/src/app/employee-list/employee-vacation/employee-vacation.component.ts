import { Component, OnInit } from '@angular/core';
import {TokenStorageService} from "../../_services/token-storage.service";
import {EmployeeDto, EmployeeService, VacationDto} from "../../../../generated";
import {Observable} from "rxjs";

@Component({
  selector: 'app-employee-vacation',
  templateUrl: './employee-vacation.component.html',
  styleUrls: ['./employee-vacation.component.css']
})
export class EmployeeVacationComponent implements OnInit {

  start:string = null;
  end:string = null;
  isLoggedIn = false;
  private roles: string[] = [];
  content?: string;
  empl: Observable<EmployeeDto>;
  employee: EmployeeDto;
  displayedColumns: string[] = ['start', 'end', 'edit'];
  userId: number;

  constructor(private tokenStorage: TokenStorageService,
              private employeeApiService: EmployeeService) {
    this.isLoggedIn = !!this.tokenStorage.getToken();
    if (this.isLoggedIn) {
      this.userId = Number.parseInt(sessionStorage.getItem("id"));
      this.employeeApiService.getEmployeeVacations(this.userId).subscribe(value => {
        let user = value;
      });

    }
  }


  ngOnInit(): void {
    this.loadVacationsByUsername()
  }

  loadVacationsByUsername() {
    this.empl = this.employeeApiService.getEmployeeVacations(this.userId);
    this.empl.subscribe(value => {
      this.employee = value;
    })
  }

  onDateChanged(event, vacId: number, startOrEnd: boolean) {
    let date = new Date(event.value).toISOString().slice(0, 10);
    let d = new Date(event.value);
    d.setMinutes(d.getMinutes() - d.getTimezoneOffset());
    d.toISOString().slice(0, 10);
    this.employee.vacations.forEach(value => {
      if (value.id == vacId) {
        if (startOrEnd) {
          // @ts-ignore
          value.start = d;
        } else {
          // @ts-ignore
          value.end = d;
        }
        this.employeeApiService.updateVacationsByEmployeeId(this.employee).subscribe(value1 => {
          this.loadVacationsByUsername()
        });
      }
    })
  }

  addNewVacation() {
    if(this.start != null && this.end != null) {
      let vac:VacationDto = {
        start: this.start,
        end: this.end,
        id: null
      };
      this.employee.vacations.push(vac);
      this.employeeApiService.updateVacationsByEmployeeId(this.employee).subscribe(value => {
        this.loadVacationsByUsername();
        this.start = null;
        this.end = null;
      })
    }
  }

  deleteVacation(id: number) {
    this.employeeApiService.deleteVacationById(id).subscribe(value => this.loadVacationsByUsername())
  }

}
