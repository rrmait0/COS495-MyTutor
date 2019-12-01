from django.urls import path
from .views import (available_appointments,
                    user_appointment_list,
                    set_availability,
                    book_appointment,
                    )

urlpatterns = [
    path('available/<username>/', available_appointments),
    path('all/<username>/', user_appointment_list),
    path('create', set_availability),
    path('book', book_appointment),
    ]
